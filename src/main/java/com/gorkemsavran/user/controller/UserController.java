package com.gorkemsavran.user.controller;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.user.controller.request.AddUserRequestDTO;
import com.gorkemsavran.user.controller.request.UpdateUserRequestDTO;
import com.gorkemsavran.user.controller.response.UserResponseDTO;
import com.gorkemsavran.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return new UserResponseDTO(userService.getUser(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse addUser(@Valid @RequestBody AddUserRequestDTO addUserRequestDTO) {
        return userService.addUser(addUserRequestDTO.toUser());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        return userService.updateUser(id, updateUserRequestDTO.toUser());
    }
}
