package com.example.testsearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    // Java 쪽? 관련 Exception 전역 처리
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleApiRequestException(Exception ex) {

        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }

    // Security 관련 Exception 중 BadCredentialsException 처리
    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsExcepion() {

        RestApiException restApiException = new RestApiException();

        restApiException.setHttpStatus(HttpStatus.UNAUTHORIZED);
        restApiException.setErrorMessage("일치하는 계정이 없습니다.");

        return new ResponseEntity<>(restApiException, HttpStatus.UNAUTHORIZED);
    }
}