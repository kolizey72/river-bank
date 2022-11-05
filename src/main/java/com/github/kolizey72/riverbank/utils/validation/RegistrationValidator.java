package com.github.kolizey72.riverbank.utils.validation;

import com.github.kolizey72.riverbank.entity.dto.RegistrationForm;
import com.github.kolizey72.riverbank.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {

    private final UserService userService;

    public RegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationForm user = (RegistrationForm) target;

        if (!user.getPassword().equals(user.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "", "Passwords must match");
        }

        if (userService.findByNameIgnoreCase(user.getName()).isPresent()) {
            errors.rejectValue("name", "", "Name is already taken");
        }

        if (userService.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Email is already taken");
        }
    }
}
