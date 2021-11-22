package com.gorkemsavran.book;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class BookServiceTests {

    @Autowired
    private BookService bookService;



    @Test
    public void whenContextBootstrapped_thenNoExceptions() {

    }

    @Test
    public void addBook() {
        Book book = new Book(
                "test",
                "test",
                BookCategory.ADVENTURE,
                LocalDate.now(),
                "test"
        );
        bookService.addBook(book);
        assertEquals(1, bookService.getAllBooks().size());
        assertEquals(book.getName(), bookService.getAllBooks().stream().findFirst().get().getName());
    }

    @Test
    public void updateBook() {
        addBook();
        bookService.updateBook(1L, new Book("" +
                "test-update",
                "test-update",
                BookCategory.ADVENTURE,
                LocalDate.now(),
                "test-update"
        ));
        assertEquals("test-update", bookService.getBook(1L).getName());
    }

    @Test
    public void deleteBook() {
        addBook();
        for (Book book : bookService.getAllBooks()) {
            bookService.deleteBook(book.getId());
        }
        assertEquals(0, bookService.getAllBooks().size());
    }


}
