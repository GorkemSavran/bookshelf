package com.gorkemsavran.scenarioTests;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class UserAddBookAndShelfTest extends AbstractScenarioTest {

    @Test
    void userAddShelf() throws Exception {
        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
        checkUserShelvesLength(1);
    }

    @Test
    void userRemoveShelf() throws Exception {
        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        deleteShelf(1)
                .andExpect(jsonPath("$.message", is("Shelf successfuly deleted")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
    }

    @Test
    void userRemoveShelf_shelfNotExist() throws Exception {
        deleteShelf(1)
                .andExpect(jsonPath("$.message", is("Shelf not found!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    @Test
    void userAddBookToShelf() throws Exception {
        addBookToUsersBooks(1);

        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Book successfuly added to shelf")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
    }

    @Test
    void userAddBookToShelf_shelfNotExist() throws Exception {
        addBookToUsersBooks(1);

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Shelf not found!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    @Test
    void userAddBookToShelf_bookNotInUsersBooks() throws Exception {
        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("User has not this book!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    @Test
    void userDeleteBookFromShelf() throws Exception {
        addBookToUsersBooks(1);

        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Book successfuly added to shelf")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        deleteBookFromShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Book successfuly removed from shelf")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));
    }

    @Test
    void userDeleteBookFromShelf_bookNotExist() throws Exception {
        addBookToUsersBooks(1);

        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Book successfuly added to shelf")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        deleteBookFromShelf(1, 132)
                .andExpect(jsonPath("$.message", is("Book not found!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    @Test
    void userDeleteBookFromShelf_shelfNotExist() throws Exception {
        addBookToUsersBooks(1);

        addShelf("shelf")
                .andExpect(jsonPath("$.message", is("Shelf added successfuly")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        addBookToShelf(1, 1)
                .andExpect(jsonPath("$.message", is("Book successfuly added to shelf")))
                .andExpect(jsonPath("$.messageType", is("SUCCESS")));

        deleteBookFromShelf(132, 1)
                .andExpect(jsonPath("$.message", is("Shelf not found!")))
                .andExpect(jsonPath("$.messageType", is("ERROR")));
    }

    private ResultActions deleteBookFromShelf(int shelfId, int bookId) throws Exception {
        return mockMvc.perform(delete("/usershelf/{shelfId}/book/{bookId}", shelfId, bookId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions deleteShelf(int shelfId) throws Exception {
        return mockMvc.perform(delete("/usershelf/{shelfId}", shelfId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions addBookToShelf(int shelfId, int bookId) throws Exception {
        return mockMvc.perform(post("/usershelf/{shelfId}/book/{bookId}", shelfId, bookId)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private void checkUserShelvesLength(int expectedLength) throws Exception {
        mockMvc.perform(get("/usershelf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(expectedLength)));
    }

    private ResultActions addShelf(String shelfName) throws Exception {
        return mockMvc.perform(post("/usershelf")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("name", shelfName));
    }

    private ResultActions addBookToUsersBooks(int bookId) throws Exception {
        return mockMvc.perform(post("/userbook")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("bookId", String.valueOf(bookId)));
    }

    @Override
    protected String getUsername() {
        return "user";
    }
}
