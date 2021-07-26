package com.tutor.tutorlab.config.aspect;

import com.tutor.tutorlab.config.response.RestResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RestResponseAspect {

    // @Around("execution(* com.tutor.tutorlab.modules.*.controller.*.*(..))")
    public RestResponse restResponseHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return new RestResponse<>(HttpStatus.OK.value(), "API 호출에 성공하였습니다.", proceedingJoinPoint.proceed());
    }

}
