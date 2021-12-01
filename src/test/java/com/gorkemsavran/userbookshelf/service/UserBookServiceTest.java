package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserBookServiceTest {

    @Mock
    BookDao bookDao;

    @InjectMocks
    UserBookService userBookService;

    User fakeUser;
    Book book1;
    Book book2;

    @BeforeEach
    void setUp() {
        fakeUser = new User(
                "username",
                "password",
                Authority.ROLE_USER,
                "email"
        );
        HashSet<UserBook> userBooks = new HashSet<>();
        book1 = new Book();
        book2 = new Book();
        ReflectionTestUtils.setField(book1, "id", 1L);
        ReflectionTestUtils.setField(book2, "id", 2L);

        UserBook userBook = new UserBook(fakeUser, book1, "review", 1.0);
        UserBook userBookWithoutReview = new UserBook(fakeUser, book2, null, null);
        userBooks.add(userBook);
        userBooks.add(userBookWithoutReview);

        HashSet<UserBook> readUsers = new HashSet<>();
        readUsers.add(userBookWithoutReview);
        book2.setReadUsers(readUsers);

        fakeUser.setUserBooks(userBooks);
    }

    @Test
    void getUserBooks() {
        List<UserBook> userBooks = userBookService.getUserBooks(fakeUser);

        assertEquals(2, userBooks.size());
        assertEquals("review", userBooks.get(0).getReview());
        assertEquals(1.0, userBooks.get(0).getRating());
    }

    @Test
    void getUserReviews() {
        List<UserBook> userReviews = userBookService.getUserReviews(fakeUser);

        assertEquals(1, userReviews.size());
        assertEquals("review", userReviews.get(0).getReview());
    }

    @Test
    void addBookToUserBooks() {
        Book book = new Book("newbook", "", BookCategory.ADVENTURE, LocalDate.MAX, "");
        ReflectionTestUtils.setField(book, "id", 3L);
        given(bookDao.get(anyLong())).willReturn(Optional.of(book));

        MessageResponse messageResponse = userBookService.addBookToUserBooks(fakeUser, 3L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Book successfuly added to user's books.", messageResponse.getMessage());
        assertEquals(3, fakeUser.getUserBooks().size());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void addBookToUserBooks_bookNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = userBookService.addBookToUserBooks(fakeUser, 3L);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book does not exist", messageResponse.getMessage());
        assertEquals(2, fakeUser.getUserBooks().size());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void removeBookFromUserBooks() {
        given(bookDao.get(anyLong())).willReturn(Optional.of(book2));

        MessageResponse messageResponse = userBookService.removeBookFromUserBooks(fakeUser, 2L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Book is successfuly removed from user's books!", messageResponse.getMessage());
        assertEquals(1, fakeUser.getUserBooks().size());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void removeBookFromUserBooks_bookNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = userBookService.removeBookFromUserBooks(fakeUser, 2L);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book does not exist!", messageResponse.getMessage());
        assertEquals(2, fakeUser.getUserBooks().size());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void addReviewAndRatingToBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book2));
        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setReview("review-update");
        addReviewAndRatingDTO.setRating(1.0);

        MessageResponse messageResponse = userBookService.addReviewAndRatingToBook(fakeUser, 2L, addReviewAndRatingDTO);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Review and rating added successfuly!", messageResponse.getMessage());
        assertEquals("review-update", ((UserBook) fakeUser.getUserBooks().toArray()[1]).getReview());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void addReviewAndRatingToBook_bookNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());
        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setReview("review-update");
        addReviewAndRatingDTO.setRating(1.0);

        MessageResponse messageResponse = userBookService.addReviewAndRatingToBook(fakeUser, 3L, addReviewAndRatingDTO);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book does not exist!", messageResponse.getMessage());
        assertNotEquals("review-update", ((UserBook) fakeUser.getUserBooks().toArray()[1]).getReview());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void addReviewAndRatingToBook_userNotHaveThisBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book2));
        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setReview("review-update");
        addReviewAndRatingDTO.setRating(1.0);

        MessageResponse messageResponse = userBookService.addReviewAndRatingToBook(fakeUser, 3L, addReviewAndRatingDTO);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("User does not have this book!", messageResponse.getMessage());
        assertNotEquals("review-update", ((UserBook) fakeUser.getUserBooks().toArray()[1]).getReview());
        then(bookDao).should().get(anyLong());
    }
}