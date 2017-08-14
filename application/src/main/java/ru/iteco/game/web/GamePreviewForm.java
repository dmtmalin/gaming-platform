package ru.iteco.game.web;


import javax.validation.constraints.NotNull;

public class GamePreviewForm {

    @NotNull
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
