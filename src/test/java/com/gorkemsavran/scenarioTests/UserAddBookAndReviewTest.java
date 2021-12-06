package com.gorkemsavran.scenarioTests;

import com.gorkemsavran.TestConfig;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.config.security.JwtRequestFilter;
import com.gorkemsavran.login.controller.LoginController;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import com.gorkemsavran.userbookshelf.controller.response.UserBookResponseDTO;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = TestConfig.class)
public class UserAddBookAndReviewTest {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        System.out.println("JwtRequest: " + context.getBean(JwtRequestFilter.class));
        System.out.println("Controller: " + context.getBean(LoginController.class));
        User user = new User("user", "user", Authority.ROLE_USER, "email@gmail.com");
        Book book = new Book("book", "author", BookCategory.ADVENTURE, LocalDate.MAX, "publisher");
        userService.addUser(user);
        bookService.addBook(book);
    }

    @Test
    void userAddBook() throws Exception {
        String token = login();
        addBookToUsersBooks(token);
        checkUserBooksLength(token, 1);
    }

    @Test
    void userRemoveBook() throws Exception {
        String token = login();
        removeBookFromUsersBooks(token);
        checkUserBooksLength(token, 0);
    }

    private void removeBookFromUsersBooks(String jwtToken) throws Exception {
        mockMvc.perform(delete("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", "1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book is successfuly removed from user's books!")));
    }

    private String login() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\": \"user\",\n" +
                                "    \"password\": \"user\"\n" +
                                "}\n"))
                .andReturn();
        return JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
    }

    private MvcResult checkUserBooksLength(String jwtToken, int expectedLength) throws Exception {
        return mockMvc.perform(get("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", "1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.class)))
                .andExpect(jsonPath("$.length()", is(expectedLength)))
                .andReturn();
    }

    private void addBookToUsersBooks(String jwtToken) throws Exception {
        mockMvc.perform(post("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", "1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book successfuly added to user's books.")));
    }

    private List<UserBookResponseDTO> extractBooksFromMvcResult(MvcResult listOfBooksResult) throws UnsupportedEncodingException {
        return JsonPath.read(listOfBooksResult.getResponse().getContentAsString(), "$");
    }

    private String extractJwtTokenFromMvcResult(MvcResult loginResult) throws UnsupportedEncodingException {
        return JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
    }
}
