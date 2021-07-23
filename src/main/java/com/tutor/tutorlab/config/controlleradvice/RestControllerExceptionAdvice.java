package com.tutor.tutorlab.config.controlleradvice;

import com.tutor.tutorlab.config.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handlerRuntimeException(RuntimeException e, HttpServletRequest req) {
        log.error("===================== RuntimeException Handling =====================");
        e.printStackTrace();
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage(), Collections.emptyList());
    }

}
