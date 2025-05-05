package com.example.card_test_app.security.controller;

import com.example.card_test_app.card.model.dto.RegistrationUserDto;
import com.example.card_test_app.security.model.AuthRequest;
import com.example.card_test_app.security.service.JwtService;
import com.example.card_test_app.security.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "User Controller", description = "API для регистрации и аутентификации пользователя")
public class UserController {


    private final UserInfoService userInfoService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserInfoService userInfoService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Запрос на регистрацию пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "409", description = "Пользователь с  таким email уже зарегистрирован")
    })
    @PostMapping("/registration")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody RegistrationUserDto userInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userInfoService.addUser(userInfo));
    }

    @Operation(summary = "Запрос на получение Jwt токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt токен успешно создан"),
            @ApiResponse(responseCode = "401", description = "Пользователь не зарегистрирован")
    })
    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(authRequest.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user request!");
        }
    }

    @Operation(summary = "Запрос на удаление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален")
    })
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userInfoService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
