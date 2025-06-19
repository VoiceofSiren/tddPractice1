package com.example.tdd1.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserControllerTest.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

}
