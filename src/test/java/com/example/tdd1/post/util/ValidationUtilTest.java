package com.example.tdd1.post.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void test1() {

        // given
        String email = "test@test.com";

        // when
        boolean result = ValidationUtil.isEmail(email);

        // then
        assertTrue(result);

    }

    @Test
    void test2() {

        // given
        String email = "test.com";

        // when
        boolean result = ValidationUtil.isEmail(email);

        // then
        assertFalse(result);

    }

}