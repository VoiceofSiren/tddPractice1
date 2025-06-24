package com.example.tdd1.global.jwt.exception;

public class JwtNullException extends Throwable {

    String message;

    public JwtNullException(String message) {
        this.message = message;
    }
}
