package com.gorkemsavran.user.dao;

import com.gorkemsavran.common.dao.IDao;
import com.gorkemsavran.user.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao implements IDao<User> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("select user from User user", User.class).getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user, User u) {
        user.update(u);
        entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    public User merge(User user) {
        return entityManager.merge(user);
    }

    public void attach(User user) {
        user = entityManager.merge(user);
    }

    public User findByUsername(String username) {
        List<User> users = entityManager.createQuery("select user from User user where user.username = :username", User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();
        if (!users.isEmpty())
            return users.get(0);
        else
            return null;
    }

    public boolean existsByUsername(String username) {
        List<User> users = entityManager.createQuery("select user from User user where user.username = :username", User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();
        return !users.isEmpty();
    }

    public boolean existsByEmail(String email) {
        List<User> users = entityManager.createQuery("select user from User user where user.email = :email", User.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();
        return !users.isEmpty();
    }
}
