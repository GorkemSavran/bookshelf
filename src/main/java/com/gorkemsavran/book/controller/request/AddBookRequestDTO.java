package com.gorkemsavran.book.controller.request;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class AddBookRequestDTO {

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String author;

    @NotNull
    private BookCategory category;

    @NotNull
    @Past
    private LocalDate publishDate;

    @NotNull
    @Size(min = 1, max = 200)
    private String publisher;

    public AddBookRequestDTO() {
    }

    public AddBookRequestDTO(String name, String author, BookCategory category, LocalDate publishDate, String publisher) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.publishDate = publishDate;
        this.publisher = publisher;
    }

    public Book toBookEntity() {
        return new Book(
                name,
                author,
                category,
                publishDate,
                publisher
        );
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
}
