package com.gorkemsavran.register.controller;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.register.controller.request.RegisterRequestDTO;
import com.gorkemsavran.register.service.RegisterService;
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
