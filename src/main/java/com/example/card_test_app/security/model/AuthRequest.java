package com.example.card_test_app.security.model;

public class AuthRequest {

    private String email;
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
