package com.gorkemsavran.login.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
