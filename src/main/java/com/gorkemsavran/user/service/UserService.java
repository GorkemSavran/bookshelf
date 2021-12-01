package com.gorkemsavran.user.service;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.dao.UserDao;
import com.gorkemsavran.user.entity.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public User getUser(Long id) {
        return userDao.get(id).orElseThrow(userNotFound());
    }

    private Supplier<EntityNotFoundException> userNotFound() {
        return () -> new EntityNotFoundException("User not found!");
    }

    @Transactional
    public MessageResponse addUser(User user) {
        User optionalUser = userDao.findByUsername(user.getUsername());
        if (optionalUser != null)
            return new MessageResponse("Username is taken!", MessageType.ERROR);
        if (isExistsByEmail(user))
            return new MessageResponse("Email is taken!", MessageType.ERROR);
        userDao.save(user);
        return new MessageResponse(String.format("User with username %s successfuly saved", user.getUsername()), MessageType.SUCCESS);
    }

    private boolean isExistsByEmail(User user) {
        return userDao.existsByEmail(user.getEmail());
    }

    @Transactional
    public MessageResponse deleteUser(Long id) {
        Optional<User> optionalUser = userDao.get(id);
        if (!optionalUser.isPresent())
            return new MessageResponse("User does not exist", MessageType.ERROR);
        userDao.delete(optionalUser.get());
        return new MessageResponse(String.format("User with username %s is successfuly deleted!", optionalUser.get().getUsername()), MessageType.SUCCESS);
    }

    @Transactional
    public MessageResponse updateUser(Long id, User updateUser) {
        Optional<User> optionalUser = userDao.get(id);
        if (!optionalUser.isPresent())
            return new MessageResponse("User that you are trying to update does not exist!", MessageType.ERROR);
        if (isExistsByUsername(updateUser))
            return new MessageResponse("Username is taken!", MessageType.ERROR);
        if (isExistsByEmail(updateUser))
            return new MessageResponse("Email is taken!", MessageType.ERROR);
        userDao.update(optionalUser.get(), updateUser);
        return new MessageResponse("User is successfuly updated!", MessageType.SUCCESS);
    }

    private boolean isExistsByUsername(User updateUser) {
        return userDao.existsByUsername(updateUser.getUsername());
    }
}
