package ru.iteco.game.web;

import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.FileNotEmpty;
import ru.iteco.constraint.ImageMinResolution;
import ru.iteco.constraint.SupportedFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameCreateForm {

    public static final String UPLOAD_ICON_PATH = "icon/";

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @FileNotEmpty
    @SupportedFormat(value = "jpg jpeg png")
    @ImageMinResolution
    private MultipartFile icon;

    @NotNull
    @Size(min = 20, max = 512)
    private String description;

    @NotNull
    private Integer categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getIcon() {
        return icon;
    }

    public void setIcon(MultipartFile icon) {
        this.icon = icon;
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
}
