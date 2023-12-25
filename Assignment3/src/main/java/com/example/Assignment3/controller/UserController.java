package com.example.Assignment3.controller;

import com.example.Assignment3.exception.UserAlreadyEnrolledException;
import com.example.Assignment3.exception.UserNotFoundException;
import com.example.Assignment3.service.AuthenticationService;
import com.example.Assignment3.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthenticationService service;

//    @PostMapping("/register")
//    public ResponseEntity<User> registerUser(@RequestParam int user_id, @RequestParam String user_name, @RequestParam String address, @RequestParam Double latitude, @RequestParam Double longitude) {
//        try {
//            User user = userService.registerUser(user_id, user_name, address, latitude, longitude);
//            return ResponseEntity.ok(user);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollForOfflinePayment(@RequestParam int user_id) {
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
