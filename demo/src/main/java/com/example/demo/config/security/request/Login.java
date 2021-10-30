package com.example.demo.config.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @NotNull
    private String usernameOrEmail;
    @NotNull
    private String password;
}
