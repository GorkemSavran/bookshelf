package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.aspect.PersistUser;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.dao.UserDao;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserBookService {

    private final BookDao bookDao;

    public UserBookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Transactional
    @PersistUser
    public List<UserBook> getUserBooks(User user) {
        return new ArrayList<>(user.getUserBooks());
    }

    @Transactional
    @PersistUser
    public List<UserBook> getUserReviews(User user) {
        return user.getUserBooks().stream().filter(this::existUserBookReview)
                .collect(Collectors.toList());
    }

    private boolean existUserBookReview(UserBook userBook) {
        return userBook.getReview() != null && userBook.getRating() != null;
    }

    @Transactional
    @PersistUser
    public MessageResponse addBookToUserBooks(User user, Long bookId) {
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist", MessageType.ERROR);

        user.getUserBooks().add(new UserBook(user, optionalBook.get(), null, null));

        return new MessageResponse("Book successfuly added to user's books.", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse removeBookFromUserBooks(User user, Long bookId) {
        Optional<Book> optionalBook = bookDao.get(bookId);
        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist!", MessageType.ERROR);

        final UserBook fakeUserBook = new UserBook(user, optionalBook.get(), null, null);
        user.getUserBooks().remove(fakeUserBook);
        optionalBook.get().getReadUsers().remove(fakeUserBook);

        return new MessageResponse("Book is successfuly removed from user's books!", MessageType.SUCCESS);
    }

    @Transactional
    @PersistUser
    public MessageResponse addReviewAndRatingToBook(User user, Long bookId, AddReviewAndRatingDTO addReviewAndRatingDTO) {
        Optional<Book> optionalBook = bookDao.get(bookId);

        if (!optionalBook.isPresent())
            return new MessageResponse("Book does not exist!", MessageType.ERROR);

        Optional<UserBook> userBook = user.getUserBooks().stream().filter(isUserBookEquals(bookId)).findFirst();

        if (!userBook.isPresent())
            return new MessageResponse("User does not have this book!", MessageType.ERROR);

        userBook.get().setReview(addReviewAndRatingDTO.getReview());
        userBook.get().setRating(addReviewAndRatingDTO.getRating());

        return new MessageResponse("Review and rating added successfuly!", MessageType.SUCCESS);
    }

    private Predicate<UserBook> isUserBookEquals(Long bookId) {
        return userBook -> userBook.getBook().getId().equals(bookId);
    }

}
