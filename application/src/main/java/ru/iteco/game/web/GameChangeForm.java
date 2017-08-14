package ru.iteco.game.web;

import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.ImageMinResolution;
import ru.iteco.constraint.SupportedFormat;
import ru.iteco.game.Game;
import ru.iteco.game.GameVersion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameChangeForm {
    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 512)
    private String description;

    @NotNull
    private Integer categoryId;

    private Integer currentGameVersionId;

    @NotNull
    @SupportedFormat(value = "jpg jpeg png")
    @ImageMinResolution
    private MultipartFile icon;

    @NotNull
    private String iconUrl;

    public GameChangeForm() {

    }

    public GameChangeForm(@NotNull Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.description = game.getDescription();
        this.categoryId = game.getGameCategoryFk();
        this.iconUrl = game.getIcon();
        GameVersion currentGameVersion = game.getCurrentGameVersion();
        if (currentGameVersion != null)
            this.currentGameVersionId = currentGameVersion.getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCurrentGameVersionId() {
        return currentGameVersionId;
    }

    public void setCurrentGameVersionId(Integer currentGameVersionId) {
        this.currentGameVersionId = currentGameVersionId;
    }

    public MultipartFile getIcon() {
        return icon;
    }

    public void setIcon(MultipartFile icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
