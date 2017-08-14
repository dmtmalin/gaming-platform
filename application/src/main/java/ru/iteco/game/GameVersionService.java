package ru.iteco.game;

import ru.iteco.game.web.GameVersionCreateForm;

public interface GameVersionService {
    GameVersion create(GameVersionCreateForm form);
}
