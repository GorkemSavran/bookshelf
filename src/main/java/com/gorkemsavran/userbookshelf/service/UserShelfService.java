package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.aspect.PersistUser;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.dao.ShelfDao;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.gorkemsavran.userbookshelf.checker.UserShelfServiceCheckers.*;

@Service
public class UserShelfService {

    private final BookDao bookDao;
    private final ShelfDao shelfDao;

    public UserShelfService(BookDao bookDao, ShelfDao shelfDao) {
        this.bookDao = bookDao;
        this.shelfDao = shelfDao;
    }

    @Transactional
    @PersistUser
    public Set<Shelf> getUserShelfs(User user) {
        return user.getShelves();
    }

    @Transactional
    @PersistUser
    public List<Book> getUserShelfBooks(User user, Long shelfId) {
        return shelfDao.getBooksOfShelf(user.getId(), shelfId).orElseThrow(shelfNotFound());
    }

    private Supplier<EntityNotFoundException> shelfNotFound() {
        return () -> new EntityNotFoundException("Shelf not found!");
    }

    @Transactional
    @PersistUser
    public MessageResponse addShelf(User user, String name) {
        user.addShelf(new Shelf(user, name));
        return new MessageResponse("Shelf added successfuly", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse deleteShelf(User user, Long shelfId) {
        Shelf shelf = getShelf(shelfId);
        checkUserHasShelf(user, shelf);

        user.removeShelf(shelf);
        return new MessageResponse("Shelf successfuly deleted", MessageType.SUCCESS);
    }

    private Shelf getShelf(Long shelfId) {
        return shelfDao.get(shelfId).orElseThrow(shelfNotFound());
    }

    @Transactional
    @PersistUser
    public MessageResponse addBookToShelf(User user, Long shelfId, Long bookId) {
        Book book = getBook(bookId);
        checkUserHasBook(user, book);
        Shelf shelf = getShelf(shelfId);
        checkUserHasShelf(user, shelf);

        shelf.addBook(book);
        return new MessageResponse("Book successfuly added to shelf", MessageType.SUCCESS);
    }

    private Book getBook(Long bookId) {
        return bookDao.get(bookId).orElseThrow(bookNotFound());
    }

    private Supplier<EntityNotFoundException> bookNotFound() {
        return () -> new EntityNotFoundException("Book not found!");
    }

    @Transactional
    @PersistUser
    public MessageResponse deleteBookFromShelf(User user, Long shelfId, Long bookId) {
        Book book = getBook(bookId);
        Shelf shelf = getShelf(shelfId);
        checkUserHasBook(user, book);
        checkUserHasShelf(user, shelf);
        checkShelfHasBook(shelf, book);

        shelf.removeBook(book);
        return new MessageResponse("Book successfuly removed from shelf", MessageType.SUCCESS);
    }
}
