package com.example.Assignment3.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialsController {
    String user_name;
    String password;

    public String getPassword() {
        return password;
    }

    public String getUser_name() {
        return user_name;
    }
}
