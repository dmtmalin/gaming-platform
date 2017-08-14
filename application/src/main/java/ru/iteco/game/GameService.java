package ru.iteco.game;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.game.web.GameChangeForm;
import ru.iteco.game.web.GameCreateForm;

import javax.validation.constraints.NotNull;

public interface GameService {

    Game create(GameCreateForm form);

    boolean canAccess(UserDetails userDetails, Integer id);

    Game update(@NotNull Game game, GameChangeForm form);
}
