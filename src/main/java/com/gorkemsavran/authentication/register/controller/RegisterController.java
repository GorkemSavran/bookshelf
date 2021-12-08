package com.gorkemsavran.authentication.register.controller;

import com.gorkemsavran.authentication.register.controller.request.RegisterRequestDTO;
import com.gorkemsavran.authentication.register.service.RegisterService;
import com.gorkemsavran.common.response.MessageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public MessageResponse register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return registerService.register(registerRequestDTO.toUser());
    }

}
