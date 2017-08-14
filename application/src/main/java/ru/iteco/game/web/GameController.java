package ru.iteco.game.web;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.game.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    private GameCategoryRepository gameCategoryRepository;
    private GameRepository gameRepository;
    private GameVersionRepository gameVersionRepository;
    private GameService gameService;
    private GameCreateFormValidator gameCreateFormValidator;
    private GameChangeFormValidator gameChangeFormValidator;

    @Autowired
    public GameController(GameCategoryRepository gameCategoryRepository,
                          GameRepository gameRepository,
                          GameVersionRepository gameVersionRepository,
                          GameService gameService,
                          GameCreateFormValidator gameCreateFormValidator,
                          GameChangeFormValidator gameChangeFormValidator) {
        this.gameCategoryRepository = gameCategoryRepository;
        this.gameRepository = gameRepository;
        this.gameVersionRepository = gameVersionRepository;
        this.gameService = gameService;
        this.gameCreateFormValidator = gameCreateFormValidator;
        this.gameChangeFormValidator = gameChangeFormValidator;
    }

    @InitBinder("gameCreateForm")
    public void bindGameCreateFormValidator(WebDataBinder binder) {
        binder.addValidators(gameCreateFormValidator);
    }

    @InitBinder("gameChangeForm")
    public void bindGameChangeFormValidator(WebDataBinder binder) {
        binder.addValidators(gameChangeFormValidator);
    }

    @GetMapping(value = "/create")
    public ModelAndView showGameCreate(Model model) {
        return new ModelAndView("game/game_create", "gameCreateForm", new GameCreateForm());
    }

    @PostMapping(value = "/create")
    public String gameCreate(@Valid @ModelAttribute("gameCreateForm") GameCreateForm form,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "game/game_create";
        }
        try {
            Game game = gameService.create(form);
            Integer id = game.getId();
            return "redirect:/game/" + id;
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("error", "Game already exist");
            return "game/game_create";
        }
    }

    @GetMapping(value = "/change/{id}")
    public ModelAndView showChangeGame(@PathVariable Integer id) {
        Game game = gameRepository.findOne(id);
        if (game == null)
            throw new EntityNotFoundException();
        return new ModelAndView("game/game_change", "gameChangeForm", new GameChangeForm(game));
    }

    @PreAuthorize("@gameServiceImpl.canAccess(principal, #id)")
    @PostMapping(value = "/change/{id}")
    public String changeGame(Model model,
                            @PathVariable Integer id,
                            @Valid @ModelAttribute("gameChangeForm") GameChangeForm form,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "game/game_change";
        }
        Game game = gameRepository.findOne(id);
        if (game == null)
            throw new EntityNotFoundException();
        game = gameService.update(game, form);
        form.setIconUrl(game.getIcon());
        model.addAttribute("game_change_success", true);
        return "game/game_change";
    }

    @PostMapping(value = "/preview")
    public ModelAndView preview(@Valid @ModelAttribute("gamePreviewForm") GamePreviewForm form) {
        return new ModelAndView("game/game_preview", "gameUri", form.getUri());
    }

    @GetMapping(value = "/{id}")
    public ModelAndView showGame(@PathVariable Integer id) {
        Game game = gameRepository.findOne(id);
        if (game == null)
            throw new EntityNotFoundException();
        return new ModelAndView("game/game", "game", game);
    }

    @Cacheable("cacheCategories")
    @ModelAttribute("categories")
    public Map<String, String> populateCategories() {
        Map<String, String> map = new HashMap<>();
        gameCategoryRepository.findAllByOrderByPriorityDesc().forEach(
                gameCategory -> map.put(
                        gameCategory.getId().toString(),
                        gameCategory.getName()
                ));
        return map;
    }

    @ModelAttribute("versions")
    public Map<String, String> populateGameVersions(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String uri = request.getRequestURI();
        String gameChangePart = "/game/change/";
        if (uri.contains(gameChangePart)) {
            Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Integer gameId = NumberUtils.toInt(pathVariables.get("id").toString(), 0);
            gameVersionRepository.findAllByGameFkAndIsApproveIsTrueOrderByBuildDesc(gameId).forEach(
                    gameVersion -> map.put(
                            gameVersion.getId().toString(),
                            "Билд " + gameVersion.getBuild().toString()
                    ));
        }
        return map;
    }
}
