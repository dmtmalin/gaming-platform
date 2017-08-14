package ru.iteco.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.CryptoUtility;
import ru.iteco.WebDavStorageService;
import ru.iteco.error.BadRequestException;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.profile.web.UserChangeForm;
import ru.iteco.profile.web.UserChangePasswordForm;
import ru.iteco.profile.web.UserCreateForm;
import ru.iteco.property.BaseProperties;
import ru.iteco.property.EmailProperties;
import ru.iteco.property.ImageThumbnailProperties;
import ru.iteco.security.UserDetailsImpl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BaseProperties baseProperties;
    private final EmailProperties emailProperties;
    private final ImageThumbnailProperties thumbnail;
    private final WebDavStorageService webDavStorageService;
    private final CryptoUtility cryptoUtility;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BaseProperties baseProperties,
                           EmailProperties emailProperties,
                           ImageThumbnailProperties thumbnailProperties,
                           WebDavStorageService webDavStorageService,
                           CryptoUtility cryptoUtility) {
        this.userRepository = userRepository;
        this.baseProperties = baseProperties;
        this.emailProperties = emailProperties;
        this.thumbnail = thumbnailProperties;
        this.webDavStorageService = webDavStorageService;
        this.cryptoUtility = cryptoUtility;
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    public User create(UserCreateForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setFullName(form.getFullName());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setRole(form.getRole());
        user.setEnabled(false);
        user.setSendNotificationActivate(false);
        return userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void lastLogin(User user) {
        Date now = new Date();
        user.setLastLogin(now);
        userRepository.save(user);
        logger.info("The last login of user is established.");
    }

    @Override
    public String generateToken(User user) throws RuntimeException {
        String secretKey = baseProperties.getSecretKey();
        Random random = new SecureRandom();
        String salt = new BigInteger(64, random).toString(32);
        String token = user.getId() + "|" + expirationTimestamp() + "|" + salt;
        String tokenEncrypt;
        try {
            tokenEncrypt = cryptoUtility.encrypt(secretKey, token);
        }
        catch (Exception e) {
            throw new RuntimeException("Can't generate user token. " + e.getMessage(), e);
        }
        return tokenEncrypt;
    }

    @Override
    public UserToken parseToken(String token) throws BadRequestException {
        try {
            String secretKey = baseProperties.getSecretKey();
            String decryptToken = cryptoUtility.decrypt(secretKey, token);
            String[] splitToken = decryptToken.split("\\|");
            Integer userId = Integer.parseInt(splitToken[0]);
            Long expirationTimestamp = Long.parseLong(splitToken[1]);
            String salt = splitToken[2];
            return new UserToken(userId, expirationTimestamp, salt);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    @Override
    public boolean tokenExpiration(UserToken token) {
        Long expirationTimestamp = token.getExpirationTimestamp();
        Long nowTimestamp = new Date().getTime();
        return nowTimestamp > expirationTimestamp;
    }

    @Override
    public void activate(UserToken token) throws EntityNotFoundException {
        Integer userId = token.getId();
        User user = userRepository.findOne(userId);
        if (user == null || user.getEnabled())
            throw new EntityNotFoundException();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(UserChangePasswordForm form) {
        User user = currentUser();
        if (user != null) {
            user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            return null;
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public User update(User user, UserChangeForm form) {
        user.setFullName(form.getFullName());
        String uploadPrefix = UserChangeForm.UPLOAD_AVATAR_PATH;
        String avatarUrl;
        MultipartFile avatar = form.getAvatar();
        if (!avatar.isEmpty()) {
            avatarUrl = webDavStorageService.store(avatar, uploadPrefix);
            user.setAvatarOrigin(avatarUrl);
            avatarUrl = webDavStorageService.thumbnail(avatar,
                    thumbnail.getWidth(),
                    thumbnail.getHeight(),
                    uploadPrefix);
            user.setAvatar(avatarUrl);
            avatarUrl = webDavStorageService.thumbnail(avatar,
                    thumbnail.getSmallWidth(),
                    thumbnail.getSmallHeight(),
                    uploadPrefix);
            user.setAvatarSmall(avatarUrl);
        }
        user = userRepository.save(user);
        reloadUser(user);
        return user;
    }

    private void reloadUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userDetails.reload(user, authentication);
        }
    }

    private Long expirationTimestamp() {
        Integer expirationHour = emailProperties.getExpirationHour();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expirationHour);
        return calendar.getTime().getTime();
    }
}
