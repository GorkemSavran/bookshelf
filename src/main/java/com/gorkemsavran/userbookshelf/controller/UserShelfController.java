package com.gorkemsavran.userbookshelf.controller;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.controller.response.UserShelfResponseDTO;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.service.UserShelfService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usershelf")
public class UserShelfController {

    private final UserShelfService userShelfService;

    public UserShelfController(UserShelfService userShelfService) {
        this.userShelfService = userShelfService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Long> getUserShelves(UsernamePasswordAuthenticationToken authentication) {
        return userShelfService.getUserShelfs((User) authentication.getPrincipal())
                .stream().map(Shelf::getId).collect(Collectors.toList());
    }

    @GetMapping("/{shelfId}")
    @PreAuthorize("hasRole('USER')")
    public UserShelfResponseDTO getUserShelf(UsernamePasswordAuthenticationToken authentication,
                                             @PathVariable Long shelfId) {
        return new UserShelfResponseDTO(shelfId, userShelfService.getUserShelfBooks((User) authentication.getPrincipal(), shelfId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public MessageResponse addShelf(UsernamePasswordAuthenticationToken authentication) {
        return userShelfService.addShelf((User) authentication.getPrincipal());
    }

    @DeleteMapping("/{shelfId}")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse deleteShelf(UsernamePasswordAuthenticationToken authentication,
                                       @PathVariable Long shelfId) {
        return userShelfService.deleteShelf((User) authentication.getPrincipal(), shelfId);
    }

    @PostMapping("/{shelfId}/book/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse addBookToShelf(UsernamePasswordAuthenticationToken authentication,
                                          @PathVariable Long shelfId,
                                          @PathVariable Long bookId) {
        return userShelfService.addBookToShelf((User) authentication.getPrincipal(), shelfId, bookId);
    }

    @DeleteMapping("/{shelfId}/book/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse deleteBookFromShelf(UsernamePasswordAuthenticationToken authentication,
                                          @PathVariable Long shelfId,
                                          @PathVariable Long bookId) {
        return userShelfService.deleteBookFromShelf((User) authentication.getPrincipal(), shelfId, bookId);
    }

}
