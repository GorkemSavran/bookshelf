package com.gorkemsavran.userbookshelf.dao;

import com.gorkemsavran.common.dao.IDao;
import com.gorkemsavran.userbookshelf.entity.Shelf;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
