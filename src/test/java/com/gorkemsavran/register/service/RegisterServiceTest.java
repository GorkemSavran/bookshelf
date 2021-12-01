package com.gorkemsavran.register.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    UserDao userDao;

    @InjectMocks
    RegisterService registerService;

    User fakeUser;

    @BeforeEach
    void setUp() {
        fakeUser = new User("username", "password", Authority.ROLE_USER, "email");
    }

    @Test
    void register() {
        given(userDao.existsByUsername(anyString())).willReturn(false);
        given(userDao.existsByEmail(anyString())).willReturn(false);

        MessageResponse messageResponse = registerService.register(fakeUser);

        assertEquals(MessageType.SUCCESS, messageResponse.getMessageType());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).should().save(any(User.class));
    }

    @Test
    void register_usernameTaken() {
        given(userDao.existsByUsername(anyString())).willReturn(true);

        MessageResponse messageResponse = registerService.register(fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Username is taken!", messageResponse.getMessage());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }

    @Test
    void register_emailTaken() {
        given(userDao.existsByUsername(anyString())).willReturn(false);
        given(userDao.existsByEmail(anyString())).willReturn(true);

        MessageResponse messageResponse = registerService.register(fakeUser);

        assertEquals(MessageType.ERROR, messageResponse.getMessageType());
        assertEquals("Email is taken!", messageResponse.getMessage());
        then(userDao).should().existsByUsername(anyString());
        then(userDao).should().existsByEmail(anyString());
        then(userDao).shouldHaveNoMoreInteractions();
    }

}