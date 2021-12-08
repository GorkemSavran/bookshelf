package com.gorkemsavran.book.entity;

import com.gorkemsavran.common.entity.BaseEntity;
import com.gorkemsavran.user.entity.User;
import com.gorkemsavran.userbookshelf.entity.UserBook;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book extends BaseEntity {

    private String name;

    private String author;

    @Enumerated(value = EnumType.STRING)
    private BookCategory category;

    private LocalDate publishDate;

    private String publisher;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
    private Set<UserBook> readUsers;

    public Book(String name, String author, BookCategory category, LocalDate publishDate, String publisher) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.readUsers = new HashSet<>();
    }

    public Book() {
    }

    public void update(Book u) {
        name = u.name;
        author = u.author;
        category = u.category;
        publishDate = u.publishDate;
        publisher = u.publisher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Set<UserBook> getReadUsers() {
        return readUsers;
    }

    public void setReadUsers(Set<UserBook> readUsers) {
        this.readUsers = readUsers;
    }
}
