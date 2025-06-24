package com.example.tdd1.global.jwt.exception;

public class JwtNonExistingException extends Throwable {

    String message;

    public JwtNonExistingException(String message) {
        this.message = message;
    }
}
