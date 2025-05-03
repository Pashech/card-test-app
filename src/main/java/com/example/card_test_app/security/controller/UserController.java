package com.example.card_test_app.security.controller;

import com.example.card_test_app.card.model.dto.RegistrationUserDto;
import com.example.card_test_app.security.model.AuthRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.service.JwtService;
import com.example.card_test_app.security.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/registration")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody RegistrationUserDto userInfo){
        return ResponseEntity.status(HttpStatus.CREATED).body(userInfoService.addUser(userInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(authRequest.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user request!");
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userInfoService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
