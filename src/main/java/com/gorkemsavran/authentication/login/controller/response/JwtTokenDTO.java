package com.gorkemsavran.authentication.login.controller.response;

public class JwtTokenDTO {

    private final String token;

    public JwtTokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
