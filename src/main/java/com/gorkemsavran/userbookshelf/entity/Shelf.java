package com.gorkemsavran.userbookshelf.entity;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.user.entity.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Book> books;

    private String name;

    public Shelf() {
    }

    public Shelf(User user, String name) {
        this.user = user;
        this.books = new HashSet<>();
        this.name = name;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public boolean hasBook(Book book) {
        return books.contains(book);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
