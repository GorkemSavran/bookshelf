package com.gorkemsavran.book.service;

import com.gorkemsavran.book.dao.BookDao;
import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class BookService {

    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    public Book getBook(Long id) {
        return bookDao.get(id).orElseThrow(bookNotFound());
    }

    private Supplier<EntityNotFoundException> bookNotFound() {
        return () -> new EntityNotFoundException("Book not found!");
    }

    @Transactional
    public List<UserBook> getReviewsOfBook(Long id) {
        return bookDao.getReviewsOfBook(id);
    }

    @Transactional
    public MessageResponse addBook(Book book) {
        if (isExistsByNameAuthorAndPublishDate(book))
            return new MessageResponse("Book that you are trying to add is already exist!", MessageType.ERROR);

        bookDao.save(book);
        return new MessageResponse(String.format("Book with name %s successfuly saved", book.getName()), MessageType.SUCCESS);
    }

    private boolean isExistsByNameAuthorAndPublishDate(Book book) {
        return bookDao.existsByNameAuthorAndPublishDate(
                book.getName(),
                book.getAuthor(),
                book.getPublishDate());
    }

    @Transactional
    public MessageResponse deleteBook(Long id) {
        Optional<Book> book = bookDao.get(id);
        if (isBookNotPresent(book))
            return new MessageResponse("Book does not exist", MessageType.ERROR);
        bookDao.delete(book.get());
        return new MessageResponse(String.format("Book with id %s is successfuly deleted!", id), MessageType.SUCCESS);
    }

    private boolean isBookNotPresent(Optional<Book> book) {
        return !book.isPresent();
    }

    @Transactional
    public MessageResponse updateBook(Long id, Book updateBook) {
        Optional<Book> optionalBook = bookDao.get(id);
        if (isBookNotPresent(optionalBook))
            return new MessageResponse("Book that you are trying to update does not exist!", MessageType.ERROR);
        if (isExistsByNameAuthorAndPublishDate(updateBook))
            return new MessageResponse("Book that you are trying to update can not have same fields with other book!", MessageType.ERROR);
        bookDao.update(optionalBook.get(), updateBook);
        return new MessageResponse("Book is successfuly updated!", MessageType.SUCCESS);
    }

}
