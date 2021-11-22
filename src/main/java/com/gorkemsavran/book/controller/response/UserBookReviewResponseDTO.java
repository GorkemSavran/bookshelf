package com.gorkemsavran.book.controller.response;

import com.gorkemsavran.userbookshelf.entity.UserBook;

public class UserBookReviewResponseDTO {

    private final String username;

    private final String review;

    private final Double rating;

    public UserBookReviewResponseDTO(UserBook userBook) {
        this.username = userBook.getUser().getUsername();
        this.review = userBook.getReview();
        this.rating = userBook.getRating();
    }

    public String getReview() {
        return review;
    }

    public Double getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }
}
