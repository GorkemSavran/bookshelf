package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.aspect.PersistUser;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.dao.UserBookDao;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserBookService {

    private final BookDao bookDao;
    private final UserBookDao userBookDao;

    public UserBookService(BookDao bookDao, UserBookDao userBookDao) {
        this.bookDao = bookDao;
        this.userBookDao = userBookDao;
    }

    @Transactional
    @PersistUser
    public List<UserBook> getUserBooks(User user) {
        return new ArrayList<>(user.getUserBooks());
    }

    @Transactional
    @PersistUser
    public List<UserBook> getUserReviews(User user) {
        return bookDao.getReviewsOfUser(user.getId());
    }

    @Transactional
    @PersistUser
    public MessageResponse addBookToUserBooks(User user, Long bookId) {
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist", MessageType.ERROR);

        Book book = optionalBook.get();
        if (user.hasBook(book))
            return new MessageResponse("Book already in user's books", MessageType.ERROR);

        user.addBook(book);
        return new MessageResponse("Book successfuly added to user's books.", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse removeBookFromUserBooks(User user, Long bookId) {
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist!", MessageType.ERROR);

        Book book = optionalBook.get();
        if (!user.hasBook(book))
            return new MessageResponse("Book is not in user's books", MessageType.ERROR);

        user.removeBook(book);
        return new MessageResponse("Book is successfuly removed from user's books!", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse addReviewAndRatingToBook(User user, Long bookId, AddReviewAndRatingDTO addReviewAndRatingDTO) {
        Optional<Book> optionalBook = bookDao.get(bookId);

        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist!", MessageType.ERROR);

        Book book = optionalBook.get();
        if (!user.hasBook(book))
            return new MessageResponse("User does not have this book!", MessageType.ERROR);

        UserBook userBook = new UserBook(user, book, addReviewAndRatingDTO.getReview(), addReviewAndRatingDTO.getRating());
        userBookDao.merge(userBook);
        return new MessageResponse("Review and rating added successfuly!", MessageType.SUCCESS);
    }
}
