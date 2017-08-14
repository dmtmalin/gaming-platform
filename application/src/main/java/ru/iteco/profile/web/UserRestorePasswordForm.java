package ru.iteco.profile.web;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserRestorePasswordForm {
    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
