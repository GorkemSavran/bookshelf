package com.gorkemsavran.scenariotests;

import com.gorkemsavran.book.controller.request.AddBookRequestDTO;
import com.gorkemsavran.book.entity.BookCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class AdminAddBookAddUserTest extends AbstractScenarioTest {

    @Test
    void adminAddBook() throws Exception {
        AddBookRequestDTO addBookRequestDTO = new AddBookRequestDTO(
                "book",
                "author",
                BookCategory.ADVENTURE,
                LocalDate.MIN,
                "publisher"
        );
        addBook(addBookRequestDTO)
                .andExpect(jsonPath("$.message", is("Book with name book successfuly saved")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
    }

    @Test
    void adminAddBook_bookAlreadyExist() throws Exception {
        AddBookRequestDTO addBookRequestDTO = new AddBookRequestDTO(
                "book",
                "author",
                BookCategory.ADVENTURE,
                LocalDate.MIN,
                "publisher"
        );
        addBook(addBookRequestDTO)
                .andExpect(jsonPath("$.message", is("Book with name book successfuly saved")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
        addBook(addBookRequestDTO)
                .andExpect(jsonPath("$.message", is("Book that you are trying to add is already exist!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    private ResultActions deleteUser() throws Exception {
        return mockMvc.perform(delete("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions addBook(AddBookRequestDTO addBookRequestDTO) throws Exception {
        return mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addBookRequestDTO)));
    }

    @Override
    protected String getUsername() {
        return "admin";
    }
}
