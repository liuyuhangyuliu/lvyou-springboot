package edu.hit.lvyouweb.controller;

import edu.hit.entity.User;
import edu.hit.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    private UserMapper userMapper;

    private User user = new User();

    @BeforeEach
    void setUp() {
        user.setPassword("aaa").setUsername("aaa");
    }

    @Test
    public void insertTest(){
        userMapper.insert(user);
    }
}