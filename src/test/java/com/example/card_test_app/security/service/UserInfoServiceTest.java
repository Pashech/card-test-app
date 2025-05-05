package com.example.card_test_app.security.service;

import com.example.card_test_app.card.model.dto.RegistrationUserDto;
import com.example.card_test_app.security.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserInfoServiceTest {


    @Autowired
    UserInfoService userInfoService;

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Test
    void addUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setFirstName("Alex");
        registrationUserDto.setLastName("Chiddy");
        registrationUserDto.setEmail("Test@gmail.com");
        registrationUserDto.setPassword("12345");
        registrationUserDto.setRoles("ROLE_USER");

        String result = userInfoService.addUser(registrationUserDto);
        assertFalse(result.isEmpty());
    }
}