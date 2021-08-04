package com.tutor.tutorlab.config.controlleradvice;

import com.tutor.tutorlab.config.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e, HttpServletRequest req) {
        log.error("===================== RuntimeException Handling =====================");
        e.printStackTrace();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        // return new ErrorResponse(HttpStatus.OK.value(), e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

}
