package com.gorkemsavran.book.controller;

import com.gorkemsavran.TestConfig;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.service.UserBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerIT {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    UserBookService userBookService;

    @Autowired
    BookController bookController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        User admin = new User(
                "admin",
                "admin",
                Authority.ROLE_ADMIN,
                "admin@gmail.com"
        );

        Book book1 = new Book(
                "book1",
                "author1",
                BookCategory.ADVENTURE,
                LocalDate.parse("2021-11-11"),
                "publisher1"
        );

        Book book2 = new Book(
                "book2",
                "author2",
                BookCategory.ADVENTURE,
                LocalDate.parse("2021-11-12"),
                "publisher2"
        );

        bookService.addBook(book1);
        bookService.addBook(book2);
        userService.addUser(admin);

        admin = userService.getUser(1L);
        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setReview("review");
        addReviewAndRatingDTO.setRating(1.0);
        userBookService.addBookToUserBooks(admin, 1L);
        userBookService.addReviewAndRatingToBook(admin, 1L, addReviewAndRatingDTO);
    }

    @Test
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(List.class)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getBook() throws Exception {
        mockMvc.perform(get("/book/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getReviewsOfBook() throws Exception {
        mockMvc.perform(get("/book/{id}/reviews", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(List.class)))
                .andExpect(jsonPath("$[0].review", is("review")))
                .andExpect(jsonPath("$[0].rating", is(1.0)));
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void addBook() throws Exception {
        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"newbook\",\n" +
                                "    \"author\": \"2r\",\n" +
                                "    \"category\": \"HORROR\",\n" +
                                "    \"publishDate\": \"2021-11-12\",\n" +
                                "    \"publisher\": \"D\"\n" +
                                "}"))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book with name newbook successfuly saved")))
                .andReturn();
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/book/{id}", 1))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book with id 1 is successfuly deleted!")))
                .andReturn();
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void deleteBook_bookNotExist() throws Exception {
        mockMvc.perform(delete("/book/{id}", 132))
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book does not exist")))
                .andReturn();
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void updateBook() throws Exception {
        mockMvc.perform(put("/book/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book1updated\",\n" +
                                "    \"author\": \"author1\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-11\",\n" +
                                "    \"publisher\": \"publisher1\"\n" +
                                "}"))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book is successfuly updated!")))
                .andReturn();

    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void updateBook_bookNotExist() throws Exception {
        mockMvc.perform(put("/book/{id}", 132)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book1updated\",\n" +
                                "    \"author\": \"author1\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-11\",\n" +
                                "    \"publisher\": \"publisher1\"\n" +
                                "}"))
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book that you are trying to update does not exist!")))
                .andReturn();
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void updateBook_bookCannotHaveSameFieldsWithOtherBook() throws Exception {
        mockMvc.perform(put("/book/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"book2\",\n" +
                                "    \"author\": \"author2\",\n" +
                                "    \"category\": \"ADVENTURE\",\n" +
                                "    \"publishDate\": \"2021-11-12\",\n" +
                                "    \"publisher\": \"publisher1\"\n" +
                                "}"))
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book that you are trying to update can not have same fields with other book!")))
                .andReturn();
    }
}