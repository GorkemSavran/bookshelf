package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.dao.UserDao;
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

@Service
public class UserShelfService {

    private final ShelfDao shelfDao;

    private final UserDao userDao;

    private final BookDao bookDao;

    public UserShelfService(ShelfDao shelfDao, UserDao userDao, BookDao bookDao) {
        this.shelfDao = shelfDao;
        this.userDao = userDao;
        this.bookDao = bookDao;
    }

    @Transactional
    public Set<Shelf> getUserShelfs(User user) {
        user = userDao.merge(user);
        return user.getShelves();
    }

    @Transactional
    public List<Book> getUserShelfBooks(User user, Long shelfId) {
        user = userDao.merge(user);
        Shelf shelf = user.getShelves().stream().filter(s -> s.getId().equals(shelfId)).findFirst().orElseThrow(EntityNotFoundException::new);
        return new ArrayList<>(shelf.getBooks());
    }

    @Transactional
    public MessageResponse addShelf(User user) {
        user = userDao.merge(user);
        user.getShelves().add(new Shelf(user));
        return new MessageResponse("Shelf added successfuly", MessageType.SUCCESS);
    }

    @Transactional
    public MessageResponse deleteShelf(User user, Long shelfId) {
        user = userDao.merge(user);
        Optional<Shelf> optionalShelf = user.getShelves().stream().filter(shelf -> shelf.getId().equals(shelfId)).findFirst();
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);
        user.getShelves().remove(optionalShelf.get());
        return new MessageResponse("Shelf successfuly deleted", MessageType.SUCCESS);
    }

    @Transactional
    public MessageResponse addBookToShelf(User user, Long shelfId, Long bookId) {
        user = userDao.merge(user);
        Optional<UserBook> optionalBook = user.getUserBooks()
                .stream().filter(userBook -> userBook.getBook().getId().equals(bookId)).findFirst();
        if (!optionalBook.isPresent())
            return new MessageResponse("Book could not found in user's books", MessageType.ERROR);
        Optional<Shelf> optionalShelf = user.getShelves().stream().filter(shelf -> shelf.getId().equals(shelfId)).findFirst();
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);

        optionalShelf.get().getBooks().add(optionalBook.get().getBook());
        return new MessageResponse("Book successfuly added to shelf", MessageType.SUCCESS);
    }

    @Transactional
    public MessageResponse deleteBookFromShelf(User user, Long shelfId, Long bookId) {
        user = userDao.merge(user);
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist", MessageType.ERROR);
        Optional<Shelf> optionalShelf = user.getShelves().stream().filter(shelf -> shelf.getId().equals(shelfId)).findFirst();
        if (!optionalShelf.isPresent())
            return new MessageResponse("Shelf does not exist", MessageType.ERROR);

        optionalShelf.get().getBooks().remove(optionalBook.get());
        return new MessageResponse("Book successfuly removed from shelf", MessageType.SUCCESS);
    }
}
