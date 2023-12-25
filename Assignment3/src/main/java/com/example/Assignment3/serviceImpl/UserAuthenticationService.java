package com.example.Assignment3.serviceImpl;

import com.example.Assignment3.controller.JwtTokenResponseController;
import com.example.Assignment3.controller.LoginCredentialsController;
import com.example.Assignment3.controller.RegistrationRequestController;
import com.example.Assignment3.model.User;
import com.example.Assignment3.repository.UserRepository;
import com.example.Assignment3.util.KeyGenerationUtils;
import com.example.Assignment3.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    @Autowired
    UserRepository repository;

    @Autowired
    KeyGenerationUtils jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public JwtTokenResponseController register(RegistrationRequestController request) {
        User user = new User();
        user.setUser_id(request.getUser_id());
        user.setName(request.getUser_name());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUser_secret(generateUserSecret());
        user.setUser_status(true);
        user.setUser_enrolled(false);
        user.setUser_enrollapproved(false);
        user.setUser_latitude(request.getLatitude());
        user.setUser_longitude(request.getLongitude());
        user.setRole(Role.USER);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        JwtTokenResponseController authenticationResponse = new JwtTokenResponseController();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }

    public JwtTokenResponseController authenticate(LoginCredentialsController request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUser_name(), request.getPassword()));
        User user = repository.findByName(request.getUser_name()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        JwtTokenResponseController authenticationResponse = new JwtTokenResponseController();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }

    public String generateUserSecret(){
        return UUID.randomUUID().toString();
    }

}