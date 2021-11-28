package com.gorkemsavran.common.aspect;


import com.gorkemsavran.user.dao.UserDao;
import com.gorkemsavran.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.function.Function;

@Aspect
@Component
public class PersistUserAspect {

    private final UserDao userDao;

    public PersistUserAspect(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Around(value = "@annotation(PersistUser) && args(user,..)")
    public Object persistUser(ProceedingJoinPoint joinPoint, User user) throws Throwable {
        user = userDao.merge(user);
        Object[] newArgs = Arrays.stream(joinPoint.getArgs()).map(changeCustomUserArg(user)).toArray();
        return joinPoint.proceed(newArgs);
    }

    private Function<Object, Object> changeCustomUserArg(User user) {
        return arg -> arg.getClass().equals(User.class) ? user : arg;
    }

}
