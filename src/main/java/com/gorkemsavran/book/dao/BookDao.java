package com.gorkemsavran.book.dao;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.dao.IDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BookDao implements IDao<Book> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> get(Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        return entityManager.createQuery("select book from Book book", Book.class).getResultList();
    }

    @Override
    public void save(Book book) {
        entityManager.persist(book);
    }

    @Override
    public void update(Book book, Book u) {
        book.update(u);
        entityManager.merge(book);
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    public boolean existsById(Long id) {
        return entityManager.find(Book.class, id) != null;
    }

    public Book findByNameAuthorAndPublishDate(String name, String author, LocalDate publishDate) {
        List<Book> books = entityManager.createQuery("select book from Book book where book.name = :name and book.author = :author and book.publishDate = :publishDate", Book.class)
                .setParameter("name", name)
                .setParameter("author", author)
                .setParameter("publishDate", publishDate)
                .setMaxResults(1)
                .getResultList();
        if (!books.isEmpty())
            return books.get(0);
        else
            return null;
    }

    public boolean existsByNameAuthorAndPublishDate(String name, String author, LocalDate publishDate) {
        List<Book> books = entityManager.createQuery("select book from Book book where book.name = :name and book.author = :author and book.publishDate = :publishDate", Book.class)
                .setParameter("name", name)
                .setParameter("author", author)
                .setParameter("publishDate", publishDate)
                .setMaxResults(1)
                .getResultList();
        return !books.isEmpty();
    }
}
