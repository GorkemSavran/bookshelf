package com.gorkemsavran.register.service;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.dao.UserDao;
import com.gorkemsavran.user.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegisterService {

    private final UserDao userDao;

    public RegisterService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public MessageResponse register(User user) {
        if (isExistsByUsername(user))
            return new MessageResponse("Username is taken!", MessageType.ERROR);
        if (isExistsByEmail(user))
            return new MessageResponse("Email is taken!", MessageType.ERROR);

        userDao.save(user);
        return new MessageResponse("You are successfuly registered", MessageType.SUCCESS);
    }

    private boolean isExistsByEmail(User user) {
        return userDao.existsByEmail(user.getEmail());
    }

    private boolean isExistsByUsername(User user) {
        return userDao.existsByUsername(user.getUsername());
    }
}
