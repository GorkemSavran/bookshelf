package com.gorkemsavran.userbookshelf.entity;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.user.entity.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserBook {

    @EmbeddedId
    private UserBookKey userBookKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    private String review;

    private Double rating;

    public UserBook() {
    }

    public UserBook(User user, Book book, String review, Double rating) {
        this.userBookKey = new UserBookKey(user.getId(), book.getId());
        this.user = user;
        this.book = book;
        this.review = review;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBook userBook = (UserBook) o;
        return Objects.equals(userBookKey, userBook.userBookKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userBookKey);
    }

    public void update(UserBook userBook) {
        review = userBook.review;
    }

    public UserBookKey getUserBookKey() {
        return userBookKey;
    }

    public void setUserBookKey(UserBookKey userBookKey) {
        this.userBookKey = userBookKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

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
