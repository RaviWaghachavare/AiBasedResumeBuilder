package com.switchproject.demo.service;

import com.switchproject.demo.dto.LoginRequest;
import com.switchproject.demo.dto.LoginResponse;
import com.switchproject.demo.model.User;
import com.switchproject.demo.repository.UserRepository;
import com.switchproject.demo.security.JwtUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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


    public ResponseEntity<String> register(User user) {
            try {
                if (userRepo.findByEmail(user.getEmail()).isPresent()) {
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body("Email Already Exists");
                }
                user.setPassword(
                        passwordEncoder.encode(user.getPassword())
                );
                userRepo.save(user);
                return ResponseEntity.ok("Registration Successful");
            }catch(Exception e){
                return ResponseEntity
                        .internalServerError()
                        .body("Something went wrong");
            }
        }


    public ResponseEntity<LoginResponse> login(LoginRequest request){
        Optional<User> user = userRepo.findByEmail(request.getEmail());

        if(user.isPresent() &&
                passwordEncoder.matches(
                        request.getPassword(),
                        user.get().getPassword()
                )){
            String token = jwtUtil.generateToken(user.get().getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        throw new RuntimeException("Invalid Credential");
    }


    public ResponseEntity<List<User>> allUsers(){
        return ResponseEntity.ok(userRepo.findAll());
    }


    public User updateUser(Long id, User user){

        User existing = userRepo.findById(id).orElseThrow(() -> new RuntimeException("user not found with id"+ id));
        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setEmail(user.getEmail());
        existing.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(existing);
    }

    public User getUserById(Long id){
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found !!!"));
    }
    }

    