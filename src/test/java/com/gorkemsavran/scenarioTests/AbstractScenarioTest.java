package com.gorkemsavran.scenarioTests;

import com.gorkemsavran.TestConfig;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringJUnitWebConfig(classes = TestConfig.class)
public abstract class AbstractScenarioTest {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    WebApplicationContext context;

    protected MockMvc mockMvc;

    protected String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        User user = new User("user", "user", Authority.ROLE_USER, "email@gmail.com");
        Book book = new Book("book", "author", BookCategory.ADVENTURE, LocalDate.MAX, "publisher");
        userService.addUser(user);
        bookService.addBook(book);
        jwtToken = login();
    }

    protected String login() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\": \"user\",\n" +
                                "    \"password\": \"user\"\n" +
                                "}\n"))
                .andReturn();
        return JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
    }
}
