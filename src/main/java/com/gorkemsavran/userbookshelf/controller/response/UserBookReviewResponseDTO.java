package com.gorkemsavran.userbookshelf.controller.response;

import com.gorkemsavran.userbookshelf.entity.UserBook;

public class UserBookReviewResponseDTO {

    private final String bookName;

    private final String review;

    private final Double rating;

    public UserBookReviewResponseDTO(UserBook userBook) {
        this.bookName = userBook.getBook().getName();
        this.review = userBook.getReview();
        this.rating = userBook.getRating();
    }

    public String getBookName() {
        return bookName;
    }

    public String getReview() {
        return review;
    }

    public Double getRating() {
        return rating;
    }
}
