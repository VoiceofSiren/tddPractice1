package com.example.tdd1.post.util;

public class ValidationUtil {

    // email validation
    public static boolean isEmail(String email) {

        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
