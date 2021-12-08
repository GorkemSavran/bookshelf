package com.gorkemsavran.config;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import com.gorkemsavran.userbookshelf.service.UserBookService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;

@Component
public class PopulateDatabase {

    private final UserService userService;

    private final BookService bookService;

    private final UserBookService userBookService;

    public PopulateDatabase(UserService userService, BookService bookService, UserBookService userBookService) {
        this.userService = userService;
        this.bookService = bookService;
        this.userBookService = userBookService;
    }

    @PostConstruct
    public void init() {
        User admin = new User(
                "admin",
                "admin",
                Authority.ROLE_ADMIN,
                "admin@gmail.com"
        );
        User user = new User(
                "user",
                "user",
                Authority.ROLE_USER,
                "user@gmail.com"
        );
        Book book1 = new Book(
                "book1",
                "author1",
                BookCategory.ADVENTURE,
                LocalDate.parse("2021-11-11"),
                "publisher1"
        );

        Book book2 = new Book(
                "book2",
                "author2",
                BookCategory.ADVENTURE,
                LocalDate.parse("2021-11-12"),
                "publisher2"
        );

        bookService.addBook(book1);
        bookService.addBook(book2);
        userService.addUser(admin);
        userService.addUser(user);

        user = userService.getUser(2L);
        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setReview("review");
        addReviewAndRatingDTO.setRating(1.0);
        userBookService.addBookToUserBooks(user, 1L);
        userBookService.addReviewAndRatingToBook(user, 1L, addReviewAndRatingDTO);
    }

}
