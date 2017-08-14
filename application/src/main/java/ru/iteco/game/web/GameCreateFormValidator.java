package ru.iteco.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.iteco.game.Game;
import ru.iteco.game.GameRepository;

@Component
public class GameCreateFormValidator implements Validator {

    private final GameRepository gameRepository;

    @Autowired
    public GameCreateFormValidator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors())
            return;
        GameCreateForm form = (GameCreateForm) target;
        Game game = gameRepository.findOneByName(form.getName());
        if (game != null) {
            errors.reject(
                    "name", String.format("Игра с именем %s существует. Выберите другое имя", form.getName()));
        }
    }
}
