package com.neworg.neworg.controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neworg.neworg.user.UserLoginToken;
import com.neworg.neworg.user.UserRegisterDTO;
import com.neworg.neworg.user.UserLoginDTO;
import com.neworg.neworg.user.User;
import com.neworg.neworg.model.RevokedToken;
import com.neworg.neworg.repository.RevokedTokenRepository;
import com.neworg.neworg.service.TokenService;
import com.neworg.neworg.user.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;
    private final RevokedTokenRepository revokedTokenRepository;
    public AuthController(UserService userService, TokenService tokenService, RevokedTokenRepository revokedTokenRepository) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.revokedTokenRepository = revokedTokenRepository;
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserRegisterDTO user) {
        User createUser = new User();
        try {
           createUser = userService.createUser(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid UserLoginDTO user) {
        User existingUser = new User();
        try {
            existingUser = userService.logarUser(user);
            String token = tokenService.generateToken(existingUser);
            if (existingUser == null) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok(new UserLoginToken(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("authorization") String token) {
        String tokenClean = token.replace("Bearer ", "");
        revokedTokenRepository.save(new RevokedToken(tokenClean));
        
        return ResponseEntity.ok().build();
    }
    
}
