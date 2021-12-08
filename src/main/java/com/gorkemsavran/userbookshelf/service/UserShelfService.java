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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    private Predicate<Shelf> isShelfEquals(Long shelfId) {
        return shelf -> shelf.getId().equals(shelfId);
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
        Optional<Shelf> optionalShelf = shelfDao.get(shelfId);
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);

        Shelf shelf = optionalShelf.get();
        if (!user.hasShelf(shelf))
            return new MessageResponse("User does not have this shelf!", MessageType.ERROR);

        user.removeShelf(shelf);
        return new MessageResponse("Shelf successfuly deleted", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse addBookToShelf(User user, Long shelfId, Long bookId) {
        Optional<UserBook> optionalBook = user.getUserBooks()
                .stream().filter(isUserBookEquals(bookId)).findFirst();
        if (!optionalBook.isPresent())
            return new MessageResponse("Book could not found in user's books", MessageType.ERROR);
        Optional<Shelf> optionalShelf = user.getShelves().stream().filter(isShelfEquals(shelfId)).findFirst();
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);

        optionalShelf.get().getBooks().add(optionalBook.get().getBook());
        return new MessageResponse("Book successfuly added to shelf", MessageType.SUCCESS);
    }

    private Predicate<UserBook> isUserBookEquals(Long bookId) {
        return userBook -> userBook.getBook().getId().equals(bookId);
    }

    @Transactional
    @PersistUser
    public MessageResponse deleteBookFromShelf(User user, Long shelfId, Long bookId) {
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist", MessageType.ERROR);
        Optional<Shelf> optionalShelf = user.getShelves().stream().filter(isShelfEquals(shelfId)).findFirst();
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);

        optionalShelf.get().getBooks().remove(optionalBook.get());
        return new MessageResponse("Book successfuly removed from shelf", MessageType.SUCCESS);
    }
}
