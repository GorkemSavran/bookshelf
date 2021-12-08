package com.gorkemsavran.authentication.login.service;

import com.gorkemsavran.authentication.login.controller.request.LoginRequestDTO;
import com.gorkemsavran.authentication.login.controller.response.JwtTokenDTO;
import com.gorkemsavran.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginService {

    @Value("${security.jwt.secret-key}")
    String secretKey;

    private final AuthenticationProvider authenticationProvider;

    public LoginService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Transactional
    public JwtTokenDTO login(LoginRequestDTO loginRequestDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        try {
            Authentication user = authenticationProvider.authenticate(usernamePasswordAuthenticationToken);
            String token = JwtUtil.generateToken(user, secretKey, 15);
            return new JwtTokenDTO(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
