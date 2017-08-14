package ru.iteco.profile.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserChangePasswordFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserChangePasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserChangePasswordForm form = (UserChangePasswordForm) target;
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("error.password", "Пароли не совпадают");
        }
    }
}
