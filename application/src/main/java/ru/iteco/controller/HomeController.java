package ru.iteco.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.iteco.error.BadRequestException;
import ru.iteco.game.Game;
import ru.iteco.game.GameRepository;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;

import java.util.Collection;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public HomeController(GameRepository gameRepository,
                          UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @GetMapping(value = { "/", "/home" })
    public String showHome() {
        return "home";
    }

    @ModelAttribute("games")
    public Collection<Game> populateGames() {
        User user = userService.currentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return gameRepository.findAllByUserFkOrderByPriority(user.getId());
    }
}
