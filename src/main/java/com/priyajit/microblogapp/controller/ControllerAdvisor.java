package com.priyajit.microblogapp.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.InvalidRequestException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReactionNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserNotFoundException.class, PostNotFoundException.class,
            ReplyNotFoundException.class, ReplyNotFoundException.class, ReactionNotFoundException.class })
    public ResponseEntity<Object> handleNotFoundExceptions(Exception e) {
        return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception e) {
        return ResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(Exception e) {
        return ResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityOwnerMismatchException.class)
    public ResponseEntity<Object> handleEntityOwnerMismatchException(Exception e) {
        return ResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

}
