package com.example.demo.config.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String address;
    @NotNull
    @Size(min = 16, max = 16)
    private String ktp_number;
    @NotNull
    private String phone_number;
    private Set<Integer> role;
}
