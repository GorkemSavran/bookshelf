package com.gorkemsavran.authentication.login.controller;

import com.gorkemsavran.authentication.login.controller.request.LoginRequestDTO;
import com.gorkemsavran.authentication.login.controller.response.JwtTokenDTO;
import com.gorkemsavran.authentication.login.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public JwtTokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return loginService.login(loginRequestDTO);
    }

}
