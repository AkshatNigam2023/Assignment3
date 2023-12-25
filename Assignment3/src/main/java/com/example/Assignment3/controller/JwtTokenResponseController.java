package com.example.Assignment3.controller;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponseController {

    private String token;

    public JwtTokenResponseController() {
    }

    public void setToken(String token) {
        this.token = token;
    }
}
