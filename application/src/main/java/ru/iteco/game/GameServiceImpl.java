package ru.iteco.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.WebDavStorageService;
import ru.iteco.game.web.GameChangeForm;
import ru.iteco.game.web.GameCreateForm;
import ru.iteco.profile.User;
import ru.iteco.profile.UserRole;
import ru.iteco.profile.UserService;
import ru.iteco.property.ImageThumbnailProperties;
import ru.iteco.security.UserDetailsImpl;

import javax.validation.constraints.NotNull;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final ImageThumbnailProperties thumbnail;
    private final WebDavStorageService webDavStorageService;
    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameServiceImpl(ImageThumbnailProperties imageThumbnailProperties,
                           WebDavStorageService webDavStorageService,
                           GameRepository gameRepository,
                           UserService userService) {
        this.thumbnail = imageThumbnailProperties;
        this.webDavStorageService = webDavStorageService;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Override
    public Game create(GameCreateForm form) {
        Game game = new Game();
        User user = userService.currentUser();
        game.setName(form.getName());
        game.setDescription(form.getDescription());
        game.setUserFk(user.getId());
        game.setGameCategoryFk(form.getCategoryId());
        game.setPublished(true);
        game.setPriority(0);
        String uploadPrefix = GameCreateForm.UPLOAD_ICON_PATH;
        String iconUrl;
        MultipartFile icon = form.getIcon();
        iconUrl = webDavStorageService.store(icon, uploadPrefix);
        game.setIconOrigin(iconUrl);
        iconUrl = webDavStorageService.thumbnail(icon,
                thumbnail.getWidth(),
                thumbnail.getHeight(),
                uploadPrefix);
        game.setIcon(iconUrl);
        iconUrl = webDavStorageService.thumbnail(icon,
                thumbnail.getSmallWidth(),
                thumbnail.getSmallHeight(),
                uploadPrefix);
        game.setIconSmall(iconUrl);
        return gameRepository.save(game);
    }

    @Override
    public Game update(@NotNull Game game, GameChangeForm form) {
        game.setName(form.getName());
        game.setDescription(form.getDescription());
        game.setGameCategoryFk(form.getCategoryId());
        game.setCurrentGameVersionFk(form.getCurrentGameVersionId());
        String uploadPrefix = GameCreateForm.UPLOAD_ICON_PATH;
        String iconUrl;
        MultipartFile icon = form.getIcon();
        if (!icon.isEmpty()) {
            iconUrl = webDavStorageService.store(icon, uploadPrefix);
            game.setIconOrigin(iconUrl);
            iconUrl = webDavStorageService.thumbnail(icon,
                    thumbnail.getWidth(),
                    thumbnail.getHeight(),
                    uploadPrefix);
            game.setIcon(iconUrl);
            iconUrl = webDavStorageService.thumbnail(icon,
                    thumbnail.getSmallWidth(),
                    thumbnail.getSmallHeight(),
                    uploadPrefix);
            game.setIconSmall(iconUrl);
        }
        gameRepository.save(game);
        return game;
    }

    @Override
    public boolean canAccess(UserDetails principal, Integer gameId) {
        String adminRole = UserRole.ADMIN.toString();
        UserDetailsImpl userDetails = (UserDetailsImpl)principal;
        if(userDetails.containsRole(adminRole))
            return true;
        Game game = gameRepository.findOne(gameId);
        return game != null && game.getUserFk().equals(userDetails.getUser().getId());
    }
}
