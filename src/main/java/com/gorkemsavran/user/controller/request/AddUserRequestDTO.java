package com.gorkemsavran.user.controller.request;

import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddUserRequestDTO {

    @NotNull
    @Size(min = 6, max = 40)
    private String username;

    @NotNull
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    private Authority role;

    @NotNull
    @Email
    private String email;

    public AddUserRequestDTO() {
    }

    public AddUserRequestDTO(String username, String password, Authority role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public User toUser() {
        return new User(
          username,
          password,
          role,
          email
        );
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

    public Authority getRole() {
        return role;
    }

    public void setRole(Authority role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
