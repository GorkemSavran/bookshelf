package com.gorkemsavran.config;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class CreateAdminOnStartUp {

    private final UserService userService;

    private final BookService bookService;

    public CreateAdminOnStartUp(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostConstruct
    public void init() {
        userService.addUser(new User(
                "admin",
                "admin",
                Authority.ROLE_ADMIN,
                "admin@gmail.com"
        ));

        bookService.addBook(new Book(
                "rew",
                "qq",
                BookCategory.ADVENTURE,
                LocalDate.now(),
                "ewqewq"
        ));

        bookService.addBook(new Book(
                "321",
                "qq",
                BookCategory.ADVENTURE,
                LocalDate.now(),
                "ewqewq"
        ));
    }

}
