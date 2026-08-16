package com.switchproject.demo.service;

import com.switchproject.demo.dto.LoginRequest;
import com.switchproject.demo.dto.LoginResponse;
import com.switchproject.demo.dto.RegisterRequest;
import com.switchproject.demo.dto.UserResponse;
import com.switchproject.demo.exception.InvalidCredentialException;
import com.switchproject.demo.exception.UserNotFoundException;
import com.switchproject.demo.model.User;
import com.switchproject.demo.repository.UserRepository;
import com.switchproject.demo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    // ================= REGISTER =================

    public ResponseEntity<String> register(RegisterRequest request) {

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email Already Exists");
        }

        User newUser = new User();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());

        // Store only hashed password
        newUser.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepo.save(newUser);

        return ResponseEntity.ok("Registration Successful");
    }


    // ================= LOGIN =================

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        Optional<User> user =
                userRepo.findByEmail(request.getEmail());

        if (user.isPresent() &&
                passwordEncoder.matches(
                        request.getPassword(),
                        user.get().getPassword()
                )) {

            String token =
                    jwtUtil.generateToken(user.get().getEmail());

            return ResponseEntity.ok(
                    new LoginResponse(token)
            );
        }

        throw new InvalidCredentialException(
                "Invalid Credential"
        );
    }


    // ================= GET ALL USERS =================

    public ResponseEntity<List<UserResponse>> allUsers() {

        List<UserResponse> users = userRepo.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }


    // ================= GET USER BY ID =================

    public ResponseEntity<UserResponse> getUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id " + id
                        )
                );

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }


    // ================= UPDATE USER =================

    public User updateUser(Long id, User user) {

        User existing = userRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id " + id
                        )
                );

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());

        existing.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepo.save(existing);
    }
}