package com.tutor.tutorlab.config.aspect;

import com.tutor.tutorlab.config.response.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.vo.User;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthAspect {

    @Pointcut("execution(* com.tutor.tutorlab.modules.*.controller.*.*(..))")
    public void pointcut() {}

    @Before("pointcut() && args(user)")
    public void checkAuth(User user) {
        if (user == null) {
            throw new UnauthorizedException();
        }
    }
}
