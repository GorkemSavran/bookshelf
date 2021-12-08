package com.gorkemsavran.userbookshelf.checker;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.exception.UserHasNotThisBookException;
import com.gorkemsavran.userbookshelf.exception.UserHasNotThisShelfException;
import org.springframework.stereotype.Component;

@Component
public class UserShelfServiceCheckers {

    public static void checkUserHasShelf(User user, Shelf shelf) {
        if (!user.hasShelf(shelf))
            throw new UserHasNotThisShelfException();
    }

    public static void checkUserHasBook(User user, Book book) {
        if (!user.hasBook(book))
            throw new UserHasNotThisBookException();
    }

    public static void checkShelfHasBook(Shelf shelf, Book book) {
        if (!shelf.hasBook(book))
            throw new UserHasNotThisBookException();
    }

}
