package com.gorkemsavran.userbookshelf.controller.response;

import com.gorkemsavran.book.controller.response.BookResponseDTO;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.userbookshelf.entity.UserBook;

public class UserBookResponseDTO extends BookResponseDTO {

    private final String review;

    private final Double rating;

    public UserBookResponseDTO(UserBook userBook) {
        super(userBook.getBook());
        this.review = userBook.getReview();
        this.rating = userBook.getRating();
    }

    public String getReview() {
        return review;
    }

    public Double getRating() {
        return rating;
    }
}
