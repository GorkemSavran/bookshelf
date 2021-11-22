package com.gorkemsavran.user.controller.response;

import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;

public class UserResponseDTO {

    private final Long id;

    private final String username;

    private final Authority role;

    private final String email;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Authority getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }


}
