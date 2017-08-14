package ru.iteco.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.ZipUtility;
import java.io.IOException;
import java.io.InputStream;


@Component
public class GameVersionCreateFormValidator implements Validator {

    private final ZipUtility zipUtility;

    @Autowired
    public GameVersionCreateFormValidator(ZipUtility zipUtility) {
        this.zipUtility = zipUtility;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameVersionCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors())
            return;
        GameVersionCreateForm form = (GameVersionCreateForm) target;
        MultipartFile archive = form.getArchive();
        if(archive.isEmpty() && form.getUri().isEmpty()) {
            errors.reject("archive", "Выберите архив или укажите ссылку на игру");
        }
        if(!validateEntryHtmlFile(archive)) {
            errors.reject("archive", "Не найден индексный файл (index.html)");
        }

    }

    private boolean validateEntryHtmlFile(MultipartFile archive) {
        if (!archive.isEmpty()) {
            try {
                InputStream stream = archive.getInputStream();
                return zipUtility.containsFile(stream, "index.html");
            } catch (IOException e) {
                return true;
            }
        }
        return true;
    }
}
