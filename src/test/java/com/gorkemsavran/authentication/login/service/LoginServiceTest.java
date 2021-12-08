package com.gorkemsavran.authentication.login.service;

import com.gorkemsavran.authentication.login.controller.request.LoginRequestDTO;
import com.gorkemsavran.authentication.login.controller.response.JwtTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    AuthenticationProvider authenticationProvider;

    @InjectMocks
    LoginService loginService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(loginService, "secretKey", "jwtSignatureOlusturmakIcinKullanilacakCokAsiriGizliKeydfdsafdsafdsafdsafdsafdsa");
    }

    @Test
    void login() {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken("username", "password");
        given(authenticationProvider.authenticate(any(Authentication.class))).willReturn(user);

        JwtTokenDTO token = loginService.login(new LoginRequestDTO());

        assertNotNull(token, "Token null geldi");
        System.out.println(token.getToken());
        then(authenticationProvider).should().authenticate(any(Authentication.class));
    }

    @Test
    void login_authenticationException() {
        given(authenticationProvider.authenticate(any(Authentication.class))).willThrow(new BadCredentialsException(""));

        JwtTokenDTO token = loginService.login(new LoginRequestDTO());

        assertNull(token, "AuthenticationException hatası fırlatılmadı");
        then(authenticationProvider).should().authenticate(any(Authentication.class));
    }
}