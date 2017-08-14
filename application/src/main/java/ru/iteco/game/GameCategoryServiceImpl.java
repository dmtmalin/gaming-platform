package ru.iteco.game;

import org.springframework.stereotype.Service;

@Service
public class GameCategoryServiceImpl implements GameCategoryService {

    private final GameCategoryRepository gameCategoryRepository;

    public GameCategoryServiceImpl(GameCategoryRepository gameCategoryRepository) {
        this.gameCategoryRepository = gameCategoryRepository;
    }

    @Override
    public GameCategory save(GameCategory gameCategory) {
        return gameCategoryRepository.save(gameCategory);
    }
}
