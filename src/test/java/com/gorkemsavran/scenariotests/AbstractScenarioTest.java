package com.gorkemsavran.scenariotests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorkemsavran.TestConfig;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;
import com.gorkemsavran.book.service.BookService;
import com.gorkemsavran.login.controller.request.LoginRequestDTO;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.user.service.UserService;
import com.jayway.jsonpath.JsonPath;
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

    private String jwtToken;

    protected static ObjectMapper objectMapper = new ObjectMapper();

    private String username, password;

    @BeforeEach
    void setUp() throws Exception {
        buildMockMvc();
        populateDatabase();
        loginAndPopulateJwtToken();
        buildMockMvcWithJwtToken();
    }

    private void buildMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private void buildMockMvcWithJwtToken() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(new JwtTokenMockMvcBuilderCustomizer(jwtToken))
                .build();
    }

    private void populateDatabase() {
        User user = new User("user", "user", Authority.ROLE_USER, "email@gmail.com");
        User admin = new User("admin", "admin", Authority.ROLE_ADMIN, "admin@gmail.com");
        Book book = new Book("book", "author", BookCategory.ADVENTURE, LocalDate.MAX, "publisher");
        Book book2 = new Book("book2", "author2", BookCategory.ADVENTURE, LocalDate.MAX, "publisher2");
        userService.addUser(user);
        userService.addUser(admin);
        bookService.addBook(book);
        bookService.addBook(book2);
    }

    protected void loginAndPopulateJwtToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO(username, username))))
                .andReturn();
        jwtToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
    }

    protected abstract void setUsername(String username);
}
