package com.gorkemsavran.book.dao;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.dao.IDao;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    public List<UserBook> getReviewsOfBook(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBook> criteriaBuilderQuery = criteriaBuilder.createQuery(UserBook.class);

        Root<UserBook> userBookRoot = criteriaBuilderQuery.from(UserBook.class);
        criteriaBuilderQuery.where(
                isBookIdEquals(id, criteriaBuilder, userBookRoot),
                hasReview(criteriaBuilder, userBookRoot),
                hasRating(criteriaBuilder, userBookRoot)
        );

        return entityManager
                .createQuery(criteriaBuilderQuery)
                .getResultList();
    }

    private Predicate hasRating(CriteriaBuilder criteriaBuilder, Root<UserBook> userBookRoot) {
        return criteriaBuilder.isNotNull(userBookRoot.get("rating"));
    }

    private Predicate hasReview(CriteriaBuilder criteriaBuilder, Root<UserBook> userBookRoot) {
        return criteriaBuilder.isNotNull(userBookRoot.get("review"));
    }

    private Predicate isBookIdEquals(Long id, CriteriaBuilder criteriaBuilder, Root<UserBook> userBookRoot) {
        return criteriaBuilder.equal(userBookRoot.get("userBookKey").get("bookId"), id);
    }
}
