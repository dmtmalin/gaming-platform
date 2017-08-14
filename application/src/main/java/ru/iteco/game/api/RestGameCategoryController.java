package ru.iteco.game.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iteco.game.GameCategory;
import ru.iteco.game.GameCategoryRepository;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api")
public class RestGameCategoryController {

    private final GameCategoryRepository gameCategoryRepository;

    @Autowired
    public RestGameCategoryController(GameCategoryRepository gameCategoryRepository) {
        this.gameCategoryRepository = gameCategoryRepository;
    }

    @GetMapping(value = "/categories")
    public Collection<GameCategory> allCategories() {
        return gameCategoryRepository.findAllByOrderByPriorityDesc();
    }
}
