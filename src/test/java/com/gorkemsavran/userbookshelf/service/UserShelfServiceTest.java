package com.gorkemsavran.userbookshelf.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.dao.ShelfDao;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.exception.UserHasNotThisBookException;
import com.gorkemsavran.userbookshelf.exception.UserHasNotThisShelfException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserShelfServiceTest {

    @Mock
    BookDao bookDao;

    @Mock
    ShelfDao shelfDao;

    @InjectMocks
    UserShelfService userShelfService;

    User fakeUser;
    Shelf shelf1;
    Shelf shelf2;
    Book book;
    Book book2;

    @BeforeEach
    void setUp() {
        fakeUser = new User(
                "username",
                "password",
                Authority.ROLE_USER,
                "email"
        );
        shelf1 = new Shelf(fakeUser, "shelf1");
        shelf2 = new Shelf(fakeUser, "shelf2");

        book = new Book("book", "author", BookCategory.ADVENTURE, LocalDate.MAX, "publisher");
        book2 = new Book("book2", "author2", BookCategory.ADVENTURE, LocalDate.MAX, "publisher2");
        ReflectionTestUtils.setField(shelf1, "id", 1L);
        ReflectionTestUtils.setField(shelf2, "id", 2L);
        ReflectionTestUtils.setField(book, "id", 1L);
        ReflectionTestUtils.setField(book2, "id", 2L);
        ReflectionTestUtils.setField(fakeUser, "id", 1L);
        shelf1.addBook(book);
        fakeUser.addShelf(shelf1);
        fakeUser.addShelf(shelf2);
        fakeUser.addBook(book);
    }

    @Test
    void getUserShelfs() {
        Set<Shelf> userShelfs = userShelfService.getUserShelfs(fakeUser);

        assertEquals(2, userShelfs.size());
    }

    @Test
    void getUserShelfBooks() {
        given(shelfDao.getBooksOfShelf(anyLong(), anyLong())).willReturn(Optional.of(Arrays.asList(book)));
        List<Book> userShelfBooks = userShelfService.getUserShelfBooks(fakeUser, 1L);

        assertEquals("book", userShelfBooks.get(0).getName());
        assertEquals(1, userShelfBooks.size());
    }

    @Test
    void getUserShelfBooks_throwException() {
        given(shelfDao.getBooksOfShelf(anyLong(), anyLong())).willThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> userShelfService.getUserShelfBooks(fakeUser, 3L));
    }

    @Test
    void addShelf() {
        MessageResponse messageResponse = userShelfService.addShelf(fakeUser, "newshelf");

        assertTrue(fakeUser.getShelves().stream().anyMatch(shelf -> shelf.getName().equals("newshelf")));
        assertEquals(3, fakeUser.getShelves().size());
    }

    @Test
    void deleteShelf() {
        given(shelfDao.get(anyLong())).willReturn(Optional.ofNullable(shelf2));

        MessageResponse messageResponse = userShelfService.deleteShelf(fakeUser, 2L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Shelf successfuly deleted", messageResponse.getMessage());
    }

    @Test
    void deleteShelf_shelfNotExist() {
        given(shelfDao.get(anyLong())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userShelfService.deleteShelf(fakeUser, 3L));
    }

    @Test
    void addBookToShelf() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book));
        given(shelfDao.get(anyLong())).willReturn(Optional.ofNullable(shelf2));

        MessageResponse messageResponse = userShelfService.addBookToShelf(fakeUser, 2L, 1L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Book successfuly added to shelf", messageResponse.getMessage());
    }

    @Test
    void addBookToShelf_bookNotInUsersBook() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book2));

        assertThrows(UserHasNotThisBookException.class, () -> userShelfService.addBookToShelf(fakeUser, 2L, 2L));
    }

    @Test
    void addBookToShelf_shelfNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book));
        given(shelfDao.get(anyLong())).willReturn(Optional.ofNullable(new Shelf(null, "nullshelf")));

        assertThrows(UserHasNotThisShelfException.class, () -> userShelfService.addBookToShelf(fakeUser, 3L, 1L));
    }

    @Test
    void deleteBookFromShelf() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book));
        given(shelfDao.get(anyLong())).willReturn(Optional.ofNullable(shelf1));

        MessageResponse messageResponse = userShelfService.deleteBookFromShelf(fakeUser, 1L, 1L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("Book successfuly removed from shelf", messageResponse.getMessage());
        assertEquals(0, shelf1.getBooks().size());
    }

    @Test
    void deleteBookFromShelf_bookNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userShelfService.deleteBookFromShelf(fakeUser, 1L, 2L));
    }

    @Test
    void deleteBookFromShelf_shelfNotExist() {
        given(bookDao.get(anyLong())).willReturn(Optional.ofNullable(book));
        given(shelfDao.get(anyLong())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userShelfService.deleteBookFromShelf(fakeUser, 3L, 1L));
    }
}