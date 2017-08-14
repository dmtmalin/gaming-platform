package ru.iteco.game.web;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.FileIsZip;
import ru.iteco.game.Game;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameVersionCreateForm{

    public static final String UPLOAD_ARCHIVE_PATH = "game/";

    @NotNull
    private Game game;

    @Size(max = 512)
    private String whatNew;

    @NotNull
    @FileIsZip
    private MultipartFile archive;

    @URL
    private String uri;

    public GameVersionCreateForm(Game game) {
        this.game = game;
    }

    public GameVersionCreateForm() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getWhatNew() {
        return whatNew;
    }

    public void setWhatNew(String whatNew) {
        this.whatNew = whatNew;
    }

    public MultipartFile getArchive() {
        return archive;
    }

    public void setArchive(MultipartFile archive) {
        this.archive = archive;
    }
}
