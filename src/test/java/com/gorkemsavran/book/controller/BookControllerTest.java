package com.gorkemsavran.book.controller;

import com.gorkemsavran.book.controller.request.AddBookRequestDTO;
import com.gorkemsavran.book.controller.response.BookResponseDTO;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
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
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.collection.IsArray.array;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController;

    MockMvc mockMvc;

    User user;
    Book book;
    UserBook userBook;

    @BeforeEach
    void setUp() {
        user = new User("username", "password", Authority.ROLE_USER, "publisher");
        book = new Book("book", "", BookCategory.ADVENTURE, LocalDate.MAX, "");
        ReflectionTestUtils.setField(book, "id", 1L);
        userBook = new UserBook(user, book, "review", 1.0);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getAllBooks() throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        books.add(book);
        given(bookService.getAllBooks()).willReturn(books);
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(book.getName())));
    }

    @Test
    void getAllBooks_empty() throws Exception {
        given(bookService.getAllBooks()).willReturn(new ArrayList<>());
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(List.class)));
    }

    @Test
    void getBook() throws Exception {
        given(bookService.getBook(anyLong())).willReturn(book);
        mockMvc.perform(get("/book/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(book.getName())));
    }

    @Test
    void getReviewsOfBook() throws Exception {
        List<UserBook> userBooks = new ArrayList<>();
        userBooks.add(userBook);
        given(bookService.getReviewsOfBook(anyLong())).willReturn(userBooks);
        mockMvc.perform(get("/book/{id}/reviews", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].review", is("review")));
    }

    @Test
    void addBook() throws Exception {
        given(bookService.addBook(any(Book.class))).willReturn(new MessageResponse(String.format("Book with name %s successfuly saved", book.getName()), MessageType.SUCCESS));
        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book\",\n" +
                                "    \"author\": \"author\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-12\",\n" +
                                "    \"publisher\": \"publisher\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book with name book successfuly saved")));
    }

    @Test
    void addBook_bookExist() throws Exception {
        given(bookService.addBook(any(Book.class))).willReturn(new MessageResponse("Book that you are trying to add is already exist!", MessageType.ERROR));
        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book\",\n" +
                                "    \"author\": \"author\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-12\",\n" +
                                "    \"publisher\": \"publisher\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book that you are trying to add is already exist!")));
    }

    @Test
    void deleteBook() throws Exception {
        given(bookService.deleteBook(anyLong())).willReturn(new MessageResponse(String.format("Book with id %s is successfuly deleted!", book.getId()), MessageType.SUCCESS));
        mockMvc.perform(delete("/book/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book with id 1 is successfuly deleted!")));
    }

    @Test
    void deleteBook_bookNotExist() throws Exception {
        given(bookService.deleteBook(anyLong())).willReturn(new MessageResponse("Book does not exist", MessageType.ERROR));
        mockMvc.perform(delete("/book/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book does not exist")));
    }

    @Test
    void updateBook() throws Exception {
        given(bookService.updateBook(anyLong(), any(Book.class))).willReturn(new MessageResponse("Book is successfuly updated!", MessageType.SUCCESS));
        mockMvc.perform(put("/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book\",\n" +
                                "    \"author\": \"author\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-12\",\n" +
                                "    \"publisher\": \"publisher\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book is successfuly updated!")));
    }
}