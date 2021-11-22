package com.gorkemsavran.book.controller;

import com.gorkemsavran.book.controller.request.AddBookRequestDTO;
import com.gorkemsavran.book.controller.request.UpdateBookRequestDTO;
import com.gorkemsavran.book.controller.response.BookResponseDTO;
import com.gorkemsavran.book.controller.response.UserBookReviewResponseDTO;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.common.response.MessageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(BookResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBook(@PathVariable Long id) {
        return new BookResponseDTO(bookService.getBook(id));
    }

    @GetMapping("/{id}/reviews")
    public List<UserBookReviewResponseDTO> getReviewsOfBook(@PathVariable Long id) {
        return bookService.getReviewsOfBook(id).stream().map(UserBookReviewResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse addBook(@Valid @RequestBody AddBookRequestDTO addBookRequestDTO) {
        return bookService.addBook(addBookRequestDTO.toBookEntity());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookRequestDTO updateBookRequestDTO) {
        return bookService.updateBook(id, updateBookRequestDTO.toBookEntity());
    }
}
