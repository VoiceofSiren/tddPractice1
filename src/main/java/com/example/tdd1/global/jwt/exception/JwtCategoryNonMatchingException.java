package com.example.tdd1.global.jwt.exception;

public class JwtCategoryNonMatchingException extends Throwable {
    String message;

    public JwtCategoryNonMatchingException(String message) {
        this.message = message;
    }
}
