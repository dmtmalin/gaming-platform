package ru.iteco.profile.web;

import org.hibernate.validator.constraints.Email;
import ru.iteco.profile.UserRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserCreateForm {
    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String fullName;

    @NotNull
    @Size(min = 6, max = 24)
    private String password = "";

    @NotNull
    @Size(min = 6, max = 24)
    private String passwordRepeated = "";

    @NotNull
    private UserRole role = UserRole.USER;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
