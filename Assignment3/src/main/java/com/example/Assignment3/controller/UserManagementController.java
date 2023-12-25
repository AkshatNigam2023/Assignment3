package com.example.Assignment3.controller;

import com.example.Assignment3.exception.UserAlreadyEnrolledException;
import com.example.Assignment3.exception.UserNotFoundException;
import com.example.Assignment3.serviceImpl.UserAuthenticationService;
import com.example.Assignment3.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usersApi")
@RequiredArgsConstructor
public class UserManagementController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserAuthenticationService service;

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollForOfflinePayment(@RequestParam(name = "user_id") int user_id) {
        try {
            userService.enrollForOfflinePayment(user_id);
            return ResponseEntity.ok("Enrolled for offline payment successfully.");
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (UserAlreadyEnrolledException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already enrolled.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<JwtTokenResponseController> register(@RequestBody RegistrationRequestController request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponseController> authenticate(@RequestBody LoginCredentialsController request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
