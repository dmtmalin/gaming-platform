package ru.iteco.profile;

import org.springframework.security.core.Authentication;
import ru.iteco.error.BadRequestException;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.profile.web.UserChangeForm;
import ru.iteco.profile.web.UserChangePasswordForm;
import ru.iteco.profile.web.UserCreateForm;

import java.util.Collection;

public interface UserService {

    User getUserById(Integer id);

    User getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

    String generateToken(User user) throws RuntimeException;

    UserToken parseToken(String token) throws BadRequestException;

    void activate(UserToken token) throws EntityNotFoundException;

    boolean tokenExpiration(UserToken token);

    void lastLogin(User user);

    void update(User user);

    void changePassword(UserChangePasswordForm form);

    User currentUser();

    User update(User user, UserChangeForm form);
}
