package com.gorkemsavran.scenarioTests;

import com.gorkemsavran.userbookshelf.controller.response.UserBookResponseDTO;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAddBookAndReviewTest extends AbstractScenarioTest {

    @Test
    void userAddBook() throws Exception {
        addBookToUsersBooks(1);
        checkUserBooksLength(1);
    }

    @Test
    void userRemoveBook() throws Exception {
        removeBookFromUsersBooks(1);
        checkUserBooksLength(0);
    }

    private void removeBookFromUsersBooks(int bookId) throws Exception {
        mockMvc.perform(delete("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", String.valueOf(bookId))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book is successfuly removed from user's books!")));
    }

    private void checkUserBooksLength(int expectedLength) throws Exception {
        mockMvc.perform(get("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", "1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.class)))
                .andExpect(jsonPath("$.length()", is(expectedLength)));
    }

    private void addBookToUsersBooks(int bookId) throws Exception {
        mockMvc.perform(post("/userbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("bookId", String.valueOf(bookId))
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
