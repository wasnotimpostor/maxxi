package com.example.demo.controller;

import com.example.demo.config.security.request.Login;
import com.example.demo.config.security.request.Register;
import com.example.demo.service.impl.AuthService;
import com.example.demo.utils.GlobalApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> login(@RequestBody @Valid Login login){
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.authService.login(login)
        );
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> register(@RequestBody @Valid Register register){
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.authService.register(register)
        );
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> userInfo(Authentication authentication){
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.authService.myInfo(authentication)
        );
    }
}