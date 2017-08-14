package ru.iteco.game.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.iteco.game.Game;
import ru.iteco.game.GameVersion;
import ru.iteco.profile.User;
import ru.iteco.property.BaseProperties;

@Component
public class GameToRestGame implements Converter<Game, RestGame> {

    private final BaseProperties baseProperties;

    @Autowired
    public GameToRestGame(BaseProperties baseProperties) {
        this.baseProperties = baseProperties;
    }

    @Override
    public RestGame convert(Game game) {
        RestGame restGame = new RestGame();
        if (game != null) {
            restGame.setId(game.getId());
            restGame.setName(game.getName());
            restGame.setDescription(game.getDescription());
            restGame.setPriority(game.getPriority());
            String mediaUrl = baseProperties.getMediaUrl();
            String iconUrl =  mediaUrl + game.getIcon();
            restGame.setIcon(iconUrl);
            String iconOriginUrl = mediaUrl + game.getIconOrigin();
            restGame.setIconOrigin(iconOriginUrl);
            String iconSmallUrl = mediaUrl + game.getIconSmall();
            restGame.setIconSmall(iconSmallUrl);
            restGame.setCreatedAt(game.getCreatedAt());
            GameVersion currentGameVersion = game.getCurrentGameVersion();
            User user = game.getUser();
            restGame.setUserFullName(user.getFullName());
            if (currentGameVersion != null) {
                String gameUri = mediaUrl + currentGameVersion.getUri();
                restGame.setBuild(currentGameVersion.getBuild());
                restGame.setUri(gameUri);
            }
        }
        return restGame;
    }
}
