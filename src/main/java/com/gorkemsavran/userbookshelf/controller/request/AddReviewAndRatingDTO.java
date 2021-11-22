package com.gorkemsavran.userbookshelf.controller.request;

import javax.validation.constraints.*;

public class AddReviewAndRatingDTO {

    @NotNull
    @Size(min = 1, max = 200)
    private String review;

    @NotNull
    @DecimalMax(value = "5.0")
    @DecimalMin(value = "0.0")
    private Double rating;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
