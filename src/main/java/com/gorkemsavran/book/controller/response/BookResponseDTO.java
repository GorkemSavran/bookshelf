package com.gorkemsavran.book.controller.response;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.book.entity.BookCategory;

public class BookResponseDTO {

    private final Long id;

    private final String name;

    private final String author;

    private final BookCategory category;

    private final String publishDate;

    private final String publisher;

    public BookResponseDTO(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.publishDate = book.getPublishDate().toString();
        this.publisher = book.getPublisher();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public BookCategory getCategory() {
        return category;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getPublisher() {
        return publisher;
    }
}
