package com.gorkemsavran.user.service;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import com.gorkemsavran.user.dao.UserDao;
import com.gorkemsavran.user.entity.Authority;
import com.gorkemsavran.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDao userDao;

    @InjectMocks
    UserService userService;

    User fakeUser;

    @BeforeEach
    void setUp() {
        fakeUser = new User(
                "username",
                "password",
                Authority.ROLE_USER,
                "email"
        );
    }

    @Test
    void getAllUsers() {
        given(userDao.getAll()).willReturn(Arrays.asList(new User(), new User()));

        List<User> allUsers = userService.getAllUsers();

        assertEquals(2, allUsers.size());
        then(userDao).should().getAll();
    }

    @Test
    void getUser() {
        given(userDao.get(anyLong())).willReturn(Optional.ofNullable(fakeUser));

        User foundUser = userService.getUser(1L);

        assertEquals("username", foundUser.getUsername());
        then(userDao).should().get(anyLong());
    }

    @Test
    void getUser_hasError() {
        given(userDao.get(anyLong())).willThrow(new EntityNotFoundException(""));

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(1L));
        then(userDao).should().get(anyLong());
    }

    @Test
    void addUser() {
        given(userDao.findByUsername(anyString())).willReturn(null);
        given(userDao.existsByEmail(anyString())).willReturn(false);

        MessageResponse messageResponse = userService.addUser(fakeUser);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("User with username username successfuly saved", messageResponse.getMessage());
        then(userDao).should().findByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).should().save(any(User.class));
    }

    @Test
    void addUser_usernameTaken() {
        given(userDao.findByUsername(anyString())).willReturn(fakeUser);

        MessageResponse messageResponse = userService.addUser(fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Username is taken!", messageResponse.getMessage());
        then(userDao).should().findByUsername(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void addUser_emailTaken() {
        given(userDao.findByUsername(anyString())).willReturn(null);
        given(userDao.existsByEmail(anyString())).willReturn(true);

        MessageResponse messageResponse = userService.addUser(fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Email is taken!", messageResponse.getMessage());
        then(userDao).should().findByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteUser() {
        given(userDao.get(anyLong())).willReturn(Optional.ofNullable(fakeUser));

        MessageResponse messageResponse = userService.deleteUser(1L);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("User with username username is successfuly deleted!", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).should().delete(any(User.class));
    }

    @Test
    void deleteUser_userNotExist() {
        given(userDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = userService.deleteUser(1L);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("User does not exist", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateUser() {
        given(userDao.get(anyLong())).willReturn(Optional.ofNullable(fakeUser));
        given(userDao.existsByUsername(anyString())).willReturn(false);
        given(userDao.existsByEmail(anyString())).willReturn(false);

        MessageResponse messageResponse = userService.updateUser(1L, fakeUser);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        assertEquals("User is successfuly updated!", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).should().update(fakeUser, fakeUser);
    }

    @Test
    void updateUser_userNotExist() {
        given(userDao.get(anyLong())).willReturn(Optional.empty());

        MessageResponse messageResponse = userService.updateUser(1L, fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("User that you are trying to update does not exist!", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateUser_usernameTaken() {
        given(userDao.get(anyLong())).willReturn(Optional.ofNullable(fakeUser));
        given(userDao.existsByUsername(anyString())).willReturn(true);

        MessageResponse messageResponse = userService.updateUser(1L, fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Username is taken!", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateUser_emailTaken() {
        given(userDao.get(anyLong())).willReturn(Optional.ofNullable(fakeUser));
        given(userDao.existsByUsername(anyString())).willReturn(false);
        given(userDao.existsByEmail(anyString())).willReturn(true);

        MessageResponse messageResponse = userService.updateUser(1L, fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Email is taken!", messageResponse.getMessage());
        then(userDao).should().get(anyLong());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }
}