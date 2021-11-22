package com.gorkemsavran.userbookshelf.controller.response;

import com.gorkemsavran.book.controller.response.BookResponseDTO;
import com.gorkemsavran.book.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserShelfResponseDTO {

    private final Long id;

    private final List<BookResponseDTO> userBooks;

    public UserShelfResponseDTO(Long id, List<Book> shelfBooks) {
        this.id = id;
        this.userBooks = shelfBooks.stream().map(BookResponseDTO::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<BookResponseDTO> getUserBooks() {
        return userBooks;
    }
}
