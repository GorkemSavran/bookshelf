package com.gorkemsavran.scenariotests;

import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.controller.response.UserBookResponseDTO;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class UserAddBookAndReviewTest extends AbstractScenarioTest {

    @Test
    void userAddBook() throws Exception {
        addBookToUsersBooks(1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book successfuly added to user's books.")));
        checkUserBooksLength(1);
    }

    @Test
    void userAddBook_bookNotExist() throws Exception {
        addBookToUsersBooks(132)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book does not exist")));
    }

    @Test
    void userRemoveBook() throws Exception {
        removeBookFromUsersBooks(1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book is successfuly removed from user's books!")));
        checkUserBooksLength(0);
    }

    @Test
    void userRemoveBook_bookNotExist() throws Exception {
        removeBookFromUsersBooks(132)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book does not exist!")));
    }

    @Test
    void userAddBookReviewAndRating() throws Exception {
        addBookToUsersBooks(1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book successfuly added to user's books.")));

        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setRating(1.0);
        addReviewAndRatingDTO.setReview("review");

        addReviewAndRatingToBook(1, addReviewAndRatingDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Review and rating added successfuly!")));
    }

    @Test
    void userAddBookReviewAndRating_notExist() throws Exception {
        addBookToUsersBooks(1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book successfuly added to user's books.")));

        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setRating(1.0);
        addReviewAndRatingDTO.setReview("review");

        addReviewAndRatingToBook(132, addReviewAndRatingDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Book does not exist!")));
    }

    @Test
    void userAddBookReviewAndRating_notInUsersBook() throws Exception {
        addBookToUsersBooks(1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Book successfuly added to user's books.")));

        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setRating(1.0);
        addReviewAndRatingDTO.setReview("review");

        addReviewAndRatingToBook(2, addReviewAndRatingDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("User does not have this book!")));
    }

    @Test
    void userAddBookReviewAndRatingNullField() throws Exception {
        addBookToUsersBooks(1);

        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setRating(1.0);
        addReviewAndRatingDTO.setReview(null);

        addReviewAndRatingToBook(1, addReviewAndRatingDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("There are some errors on field validation")));
    }

    @Test
    void userGetUserReviews() throws Exception {
        addBookToUsersBooks(1);

        AddReviewAndRatingDTO addReviewAndRatingDTO = new AddReviewAndRatingDTO();
        addReviewAndRatingDTO.setRating(1.0);
        addReviewAndRatingDTO.setReview("review");

        addReviewAndRatingToBook(1, addReviewAndRatingDTO);

        getUserReviews()
                .andExpect(jsonPath("$.length()", is(1)));
    }

    private ResultActions getUserReviews() throws Exception {
        return mockMvc.perform(get("/userbook/reviews")
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions addReviewAndRatingToBook(int bookId, AddReviewAndRatingDTO addReviewAndRatingDTO) throws Exception {
        return mockMvc.perform(post("/userbook/add-review")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("bookId", String.valueOf(bookId))
                .content(objectMapper.writeValueAsString(addReviewAndRatingDTO)));
    }

    private ResultActions removeBookFromUsersBooks(int bookId) throws Exception {
        return mockMvc.perform(delete("/userbook")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("bookId", String.valueOf(bookId)));
    }

    private void checkUserBooksLength(int expectedLength) throws Exception {
        mockMvc.perform(get("/userbook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.class)))
                .andExpect(jsonPath("$.length()", is(expectedLength)));
    }

    private ResultActions addBookToUsersBooks(int bookId) throws Exception {
        return mockMvc.perform(post("/userbook")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("bookId", String.valueOf(bookId)));
    }

    private List<UserBookResponseDTO> extractBooksFromMvcResult(MvcResult listOfBooksResult) throws UnsupportedEncodingException {
        return JsonPath.read(listOfBooksResult.getResponse().getContentAsString(), "$");
    }

    private String extractJwtTokenFromMvcResult(MvcResult loginResult) throws UnsupportedEncodingException {
        return JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
    }

    @Override
    protected void setUsername(String username) {
        username = "user";
    }
}
