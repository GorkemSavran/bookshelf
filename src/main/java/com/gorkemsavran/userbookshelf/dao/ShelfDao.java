package com.gorkemsavran.userbookshelf.dao;

import com.gorkemsavran.book.entity.Book;
import com.gorkemsavran.common.dao.IDao;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ShelfDao implements IDao<Shelf> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Shelf> get(Long id) {
        return Optional.ofNullable(entityManager.find(Shelf.class, id));
    }

    @Override
    public List<Shelf> getAll() {
        return entityManager.createQuery("select shelf from Shelf shelf", Shelf.class).getResultList();
    }

    @Override
    public void save(Shelf shelf) {
        entityManager.persist(shelf);
    }

    @Override
    public void update(Shelf shelf, Shelf u) {
        entityManager.merge(shelf);
    }

    @Override
    public void delete(Shelf shelf) {
        entityManager.remove(shelf);
    }

    public Optional<List<Book>> getBooksOfShelf(Long userId, Long shelfId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shelf> criteriaBuilderQuery = criteriaBuilder.createQuery(Shelf.class);

        Root<Shelf> shelfRoot = criteriaBuilderQuery.from(Shelf.class);
        criteriaBuilderQuery.where(
                isShelfIdEquals(shelfId, criteriaBuilder, shelfRoot),
                isUserIdEquals(userId, criteriaBuilder, shelfRoot)
        );

        List<Shelf> shelves = getSingleElementShelfList(criteriaBuilderQuery);

        if (shelves.isEmpty())
            return Optional.empty();

        return Optional.of(
                new ArrayList<>(shelves
                        .get(0)
                        .getBooks()
                )
        );
    }

    private List<Shelf> getSingleElementShelfList(CriteriaQuery<Shelf> criteriaBuilderQuery) {
        return entityManager
                .createQuery(criteriaBuilderQuery)
                .setMaxResults(1)
                .getResultList();
    }

    private Predicate isUserIdEquals(Long userId, CriteriaBuilder criteriaBuilder, Root<Shelf> shelfRoot) {
        return criteriaBuilder.equal(shelfRoot.get("user").get("id"), userId);
    }

    private Predicate isShelfIdEquals(Long shelfId, CriteriaBuilder criteriaBuilder, Root<Shelf> shelfRoot) {
        return criteriaBuilder.equal(shelfRoot.get("id"), shelfId);
    }
}
