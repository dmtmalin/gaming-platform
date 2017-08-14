package ru.iteco.profile.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserChangePasswordForm {

    @NotNull
    @Size(min = 6, max = 24)
    private String password = "";

    @NotNull
    @Size(min = 6, max = 24)
    private String passwordRepeated = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }
}
