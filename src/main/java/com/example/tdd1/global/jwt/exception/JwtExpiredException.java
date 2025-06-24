package com.example.tdd1.global.jwt.exception;

public class JwtExpiredException extends Throwable {
    String message;

    public JwtExpiredException(String message) {
        this.message = message;
    }
}
