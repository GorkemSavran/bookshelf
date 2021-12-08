package com.gorkemsavran.userbookshelf.dao;

import com.gorkemsavran.common.dao.IDao;
import com.gorkemsavran.userbookshelf.entity.UserBook;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class UserBookDao implements IDao<UserBook> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserBook> get(Long id) {
        return Optional.ofNullable(entityManager.find(UserBook.class, id));
    }

    @Override
    public List<UserBook> getAll() {
        return entityManager.createQuery("select userbook from UserBook userbook", UserBook.class).getResultList();
    }

    @Override
    public void save(UserBook userBook) {
        entityManager.persist(userBook);
    }

    @Override
    public void update(UserBook userBook, UserBook u) {
        userBook.update(u);
        entityManager.merge(userBook);
    }

    @Override
    public void delete(UserBook userBook) {
        entityManager.remove(userBook);
    }

    public void merge(UserBook userBook) {
        entityManager.merge(userBook);
    }
}
