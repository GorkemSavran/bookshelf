package com.gorkemsavran.userbookshelf.controller;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.controller.response.UserBookResponseDTO;
import com.gorkemsavran.userbookshelf.controller.response.UserBookReviewResponseDTO;
import com.gorkemsavran.userbookshelf.service.UserBookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/userbook")
public class UserBookController {

    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<UserBookResponseDTO> getUserBooks(UsernamePasswordAuthenticationToken authentication) {
        return userBookService.getUserBooks((User) authentication.getPrincipal())
                .stream().map(UserBookResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public MessageResponse addBookToUserBooks(UsernamePasswordAuthenticationToken authentication, @RequestParam Long bookId) {
        return userBookService.addBookToUserBooks((User) authentication.getPrincipal(), bookId);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public MessageResponse removeBookFromUserBooks(UsernamePasswordAuthenticationToken authentication, @RequestParam Long bookId) {
        return userBookService.removeBookFromUserBooks((User) authentication.getPrincipal(), bookId);
    }

    @PostMapping("/add-review")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse addReviewAndRatingToBook(UsernamePasswordAuthenticationToken authentication,
                                                    @Valid @RequestBody AddReviewAndRatingDTO addReviewAndRatingDTO,
                                                    @RequestParam Long bookId) {
        return userBookService.addReviewAndRatingToBook((User) authentication.getPrincipal(), bookId, addReviewAndRatingDTO);
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasRole('USER')")
    public List<UserBookReviewResponseDTO> getUserReviews(UsernamePasswordAuthenticationToken authentication) {
        return userBookService
                .getUserReviews((User) authentication.getPrincipal())
                .stream()
                .map(UserBookReviewResponseDTO::new)
                .collect(Collectors.toList());
    }
}
