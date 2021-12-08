package com.gorkemsavran.book.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookDao bookDao;

    @InjectMocks
    BookService bookService;

    Book exampleBook;

    @BeforeEach
    void setUp() {
        exampleBook = new Book(
                "book",
                "author",
                BookCategory.ADVENTURE,
                LocalDate.MAX,
                "publisher"
        );
        User user = new User("username", "password", Authority.ROLE_USER, "email");
        UserBook userBook = new UserBook(user, exampleBook, "review", 1.0);
        HashSet<UserBook> readUsers = new HashSet<>();
        readUsers.add(userBook);
        exampleBook.setReadUsers(readUsers);
    }

    @Test
    void getAllBooks() {
        given(bookDao.getAll()).willReturn(Arrays.asList(exampleBook, new Book()));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
        then(bookDao).should().getAll();
    }

    @Test
    void getBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.of(exampleBook));

        Book foundBook = bookService.getBook(1L);

        assertEquals(exampleBook.getName(), foundBook.getName(), "Kitap ismi doğru değil.");
        then(bookDao).should().get(anyLong());
    }

    @Test
    void getBook_bookNotFound() {
        given(bookDao.get(anyLong())).willThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBook(1L), "Hata fırlatmıyor");
        then(bookDao).should().get(anyLong());
    }

    @Test
    void getReviewsOfBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(exampleBook));

        List<UserBook> reviewsOfBook = bookService.getReviewsOfBook(1L);

        assertEquals(1, reviewsOfBook.size());
        then(bookDao).should().get(anyLong());
    }

    @Test
    void getReviewsOfBook_bookNotFound() {
        given(bookDao.get(anyLong())).willThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> bookService.getReviewsOfBook(1L));
        then(bookDao).should().get(anyLong());
    }

    @Test
    void addBook() {
        given(bookDao.existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class))).willReturn(false);

        MessageResponse messageResponse = bookService.addBook(exampleBook);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        then(bookDao).should().existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class));
        then(bookDao).should().save(any(Book.class));
    }

    @Test
    void addBook_bookAlreadyExist() {
        given(bookDao.existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class))).willReturn(true);

        MessageResponse messageResponse = bookService.addBook(exampleBook);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book that you are trying to add is already exist!", messageResponse.getMessage());
        then(bookDao).should().existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class));
        then(bookDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(exampleBook));

        MessageResponse messageResponse = bookService.deleteBook(1L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        then(bookDao).should().get(anyLong());
        then(bookDao).should().delete(any(Book.class));
    }

    @Test
    void deleteBook_bookNotPresent() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = bookService.deleteBook(1L);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        then(bookDao).should().get(anyLong());
        then(bookDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(exampleBook));
        given(bookDao.existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class))).willReturn(false);

        MessageResponse messageResponse = bookService.updateBook(1L, new Book("", "", BookCategory.ADVENTURE, LocalDate.MAX, ""));

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        then(bookDao).should().get(anyLong());
        then(bookDao).should().existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class));
        then(bookDao).should().update(any(Book.class), any(Book.class));
    }

    @Test
    void updateBook_bookNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = bookService.updateBook(1L, new Book());

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book that you are trying to update does not exist!", messageResponse.getMessage());
        then(bookDao).should().get(anyLong());
        then(bookDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateBook_bookHasSameFieldsWithOtherBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(exampleBook));
        given(bookDao.existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class))).willReturn(true);

        MessageResponse messageResponse = bookService.updateBook(1L, new Book("", "", BookCategory.ADVENTURE, LocalDate.MAX, ""));

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Book that you are trying to update can not have same fields with other book!", messageResponse.getMessage());
        then(bookDao).should().get(anyLong());
        then(bookDao).should().existsByNameAuthorAndPublishDate(anyString(), anyString(), any(LocalDate.class));
        then(bookDao).shouldHaveNoMoreInteractions();
    }
}