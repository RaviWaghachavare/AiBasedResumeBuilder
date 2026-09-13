package com.switchproject.demo.controller;

import com.switchproject.demo.dto.LoginRequest;
import com.switchproject.demo.dto.LoginResponse;
import com.switchproject.demo.dto.RegisterRequest;
import com.switchproject.demo.dto.UpdateUserRequest;
import com.switchproject.demo.dto.UserResponse;
import com.switchproject.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final UserService userService;

    public ProjectController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> userLogin(
            @RequestBody LoginRequest request) {

        return userService.login(request);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        return userService.updateUser(id, request);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return userService.allUsers();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<String> deleteUserById(
            @PathVariable Long id) {

        return userService.deleteUserById(id);
    }
}