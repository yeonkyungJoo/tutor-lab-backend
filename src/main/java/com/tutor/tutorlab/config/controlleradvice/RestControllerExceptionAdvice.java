package com.tutor.tutorlab.config.controlleradvice;

import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.config.response.ErrorResponse;
import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        log.error("===================== RuntimeException Handling =====================");
        e.printStackTrace();
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생하였습니다.", Collections.singletonList(e.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ErrorResponse handleException(Exception e) {
//        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생하였습니다.", Collections.singletonList(e.getMessage()));
//    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        return ErrorResponse.of(ErrorCode.UNAUTHENTICATED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        return ErrorResponse.of(e.getErrorCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return ErrorResponse.of(e.getErrorCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistException.class)
    public ErrorResponse handleAlreadyExistException(AlreadyExistException e) {
        return ErrorResponse.of(e.getErrorCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindException(BindException e, HttpServletRequest req) {
        log.error("===================== BindException Handling =====================");
        e.printStackTrace();
        return getErrorResponse(e.getBindingResult(), HttpStatus.BAD_REQUEST, "유효하지 않는 값이 있습니다.");
    }

    private ErrorResponse getErrorResponse(BindingResult bindingResult, HttpStatus httpStatus, String defaultMessage) {
        List<String> errorMessages = Optional.ofNullable(bindingResult.getAllErrors()).orElse(Collections.emptyList())
                .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        return ErrorResponse.of(httpStatus.value(), defaultMessage, errorMessages);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.of(ErrorCode.INVALID_INPUT, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
