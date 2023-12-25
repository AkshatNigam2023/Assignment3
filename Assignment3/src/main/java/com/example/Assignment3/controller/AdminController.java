package com.example.Assignment3.controller;


import com.example.Assignment3.model.User;
import com.example.Assignment3.exception.UserAlreadyApprovedException;
import com.example.Assignment3.exception.UserNotFoundException;
import com.example.Assignment3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @PostMapping("/approve/{user_id}")
    public ResponseEntity<String> approveUser(@PathVariable int user_id) {
        try {
            User approvedUser = userService.approveUser(user_id);
            return ResponseEntity.ok("User with ID " + approvedUser.getUser_id() + " has been approved.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (UserAlreadyApprovedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already approved");
        }
    }
}
