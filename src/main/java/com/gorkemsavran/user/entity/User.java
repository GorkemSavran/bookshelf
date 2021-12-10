package com.gorkemsavran.user.entity;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.entity.UserBook;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User extends BaseUser {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<UserBook> userBooks;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Shelf> shelves;

    public User() {
    }

    public User(String username, String password, Authority role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.userBooks = new HashSet<>();
        this.shelves = new HashSet<>();
    }

    public void update(User user) {
        username = user.username;
        password = user.password;
        role = user.role;
        email = user.email;
    }

    public void addBook(Book book) {
        userBooks.add(new UserBook(this, book));
    }

    public void removeBook(Book book) {
        userBooks.remove(new UserBook(this, book));
    }

    public boolean hasBook(Book book) {
        return userBooks.contains(new UserBook(this, book));
    }

    public void addShelf(Shelf shelf) {
        shelves.add(shelf);
    }

    public void removeShelf(Shelf shelf) {
        shelves.remove(shelf);
    }

    public boolean hasShelf(Shelf shelf) {
        return shelves.contains(shelf);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }

    public Set<UserBook> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(Set<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    public Set<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(Set<Shelf> shelves) {
        this.shelves = shelves;
    }
}
