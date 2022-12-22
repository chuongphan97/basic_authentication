package com.example.authentication.api.admin.authentication.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SignInDTO {

    @Email(message = "Email is valid")
    @NotEmpty(message = "Email must be not empty")
    private String email;

    @NotEmpty(message = "Password must be not empty")
    private String password;
}