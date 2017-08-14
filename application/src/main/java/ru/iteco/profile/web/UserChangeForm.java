package ru.iteco.profile.web;

import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.ImageMinResolution;
import ru.iteco.constraint.SupportedFormat;
import ru.iteco.profile.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserChangeForm {

    public static final String UPLOAD_AVATAR_PATH = "avatar/";

    @NotNull
    @Size(min = 1, max = 255)
    private String fullName;

    @NotNull
    @SupportedFormat(value = "jpg jpeg png")
    @ImageMinResolution
    private MultipartFile avatar;

    private String avatarUrl;

    public UserChangeForm(User user) {
        this.avatarUrl = user.getAvatar();
        this.fullName = user.getFullName();
    }

    public UserChangeForm() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
