package com.github.kolizey72.riverbank.entity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistrationForm {

    @Size(min = 3, max = 32, message = "Username length must be 3-32")
    @Getter @Setter
    private String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    @Getter @Setter
    private String email;

    @Size(min = 8, message = "Password must be 8 digits min")
    @Getter @Setter
    private String password;

    @Getter @Setter
    private String matchingPassword;
}
