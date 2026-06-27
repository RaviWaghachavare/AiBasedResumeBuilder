package com.switchproject.demo.controller;

import com.switchproject.demo.dto.LoginRequest;
import com.switchproject.demo.model.User;
import com.switchproject.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class projectController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public String register(@RequestBody User user){

        if(userRepo.findByEmail(user.getEmail()).isPresent()){
            return "Email Already Exists !!!😐😐😐😂😂😂 Please use different email...!!";
        }
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        userRepo.save(user);
        return "Registration Successfully...!!!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        Optional<User> user = userRepo.findByEmail(request.getEmail());

        if(user.isPresent() &&
        passwordEncoder.matches(
                request.getPassword(),
                user.get().getPassword()
        )){
            return "Login Successful!!";
        }
        return "Invalid Credential";
    }

    @GetMapping("/public/hello")
    public String publicApi(){
        return "Anyone can access";
    }

    @GetMapping("/private/hello")
    public String privateApi(){
        return "Authenticated user only";
    }

    @GetMapping("/users")
    public List<User> getAllusers(){
        return userRepo.findAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id){
        return userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found By Id"));
    }

    @DeleteMapping("/users/{id}")
    public String deleteById(@PathVariable Long id){
        userRepo.deleteById(id);
        return "User deleted";
    }

    @PutMapping("/user/{id}")
    public User updateuser(@PathVariable Long id, @RequestBody User user){
    User existing = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    existing.setUsername(user.getUsername());
    existing.setPassword(
            passwordEncoder.encode(user.getPassword())
            );
    existing.setEmail(user.getEmail());
    return userRepo.save(existing);
    }
}
