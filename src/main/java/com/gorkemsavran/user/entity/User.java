package com.gorkemsavran.user.entity;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.entity.BaseEntity;
import com.gorkemsavran.userbookshelf.controller.request.AddReviewAndRatingDTO;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Authority role;

    @Column(unique = true)
    private String email;

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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> getRole().toString());
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Authority getRole() {
        return role;
    }

    public void setRole(Authority role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
