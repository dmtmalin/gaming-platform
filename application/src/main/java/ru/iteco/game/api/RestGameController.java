package ru.iteco.game.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iteco.game.Game;
import ru.iteco.game.GameRepository;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping(value = "/api")
public class RestGameController {
    private final GameRepository gameRepository;
    private final GameToRestGame gameToRestGame;

    @Autowired
    public RestGameController(GameRepository gameRepository, GameToRestGame gameToRestGame) {
        this.gameRepository = gameRepository;
        this.gameToRestGame = gameToRestGame;
    }

    @GetMapping(value = "/games/category/{id}")
    public Collection<RestGame> gamesByCategory(@PathVariable Integer id) {
        Collection<Game> games = gameRepository
                .findAllByGameCategoryFkAndIsPublishedIsTrueAndCurrentGameVersionIsNotNull(id);
        Collection<RestGame> restGames = new ArrayList<>();
        games.forEach(game -> restGames.add(gameToRestGame.convert(game)));
        return restGames;
    }
}
