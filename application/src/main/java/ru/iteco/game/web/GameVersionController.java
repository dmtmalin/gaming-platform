package ru.iteco.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.game.Game;
import ru.iteco.game.GameRepository;
import ru.iteco.game.GameVersion;
import ru.iteco.game.GameVersionService;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/game")
public class GameVersionController {

    private final GameRepository gameRepository;
    private final GameVersionService gameVersionService;
    private final GameVersionCreateFormValidator gameCreateFormValidator;

    @Autowired
    public GameVersionController(GameRepository gameRepository,
                                 GameVersionService gameVersionService,
                                 GameVersionCreateFormValidator gameVersionCreateFormValidator) {
        this.gameRepository = gameRepository;
        this.gameVersionService = gameVersionService;
        this.gameCreateFormValidator = gameVersionCreateFormValidator;
    }

    @InitBinder("gameVersionCreateForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(gameCreateFormValidator);
    }

    @GetMapping(value = "/{id}/version/create")
    public ModelAndView showCreateGameVersion(@PathVariable Integer id) {
        Game game = gameRepository.findOne(id);
        if (game == null) {
            throw new EntityNotFoundException();
        }
        else {
            return new ModelAndView(
                    "game/game_version_create", "gameVersionCreateForm", new GameVersionCreateForm(game));
        }
    }

    @PostMapping(value = "/{id}/version/create")
    public String createGameVersion(@Valid @ModelAttribute("gameVersionCreateForm") GameVersionCreateForm form,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "game/game_version_create";
        }
        try {
            GameVersion gameVersion = gameVersionService.create(form);
            return "redirect:/game/" + gameVersion.getGameFk();
        }
        catch (DataIntegrityViolationException e) {
            bindingResult.reject("error", "Game version already exist");
            return "game/game_version_create";
        }
    }
}
