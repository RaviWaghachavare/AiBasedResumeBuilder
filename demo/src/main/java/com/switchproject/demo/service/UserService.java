package com.switchproject.demo.service;

import com.switchproject.demo.dto.*;
import com.switchproject.demo.exception.InvalidCredentialException;
import com.switchproject.demo.exception.UserNotFoundException;
import com.switchproject.demo.model.Role;
import com.switchproject.demo.model.User;
import com.switchproject.demo.repository.UserRepository;
import com.switchproject.demo.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.switchproject.demo.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpService otpService;

    public UserService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    private final Map<String, PendingRegistration> pendingRegistrations =
            new ConcurrentHashMap<>();

    // ================= REGISTER =================

    public ResponseEntity<String> register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        String otp = otpService.generateOtp(email);

        PendingRegistration pending = new PendingRegistration(
                request.getUsername(),
                email,
                passwordEncoder.encode(request.getPassword())
        );

        pendingRegistrations.put(email, pending);

        try {
            emailService.sendEmail(
                    email,
                    "AI Resume Builder - Email Verification OTP",
                    "Your OTP is: " + otp
                            + "\nThis OTP expires in 5 minutes."
            );
        } catch (Exception e) {
            pendingRegistrations.remove(email);
            otpService.verifyOtp(email, "000000");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to send OTP email. Please try again.");
        }

        return ResponseEntity.ok("OTP sent to your email. Please verify it.");
    }

    public ResponseEntity<String> verifyOtp(VerifyOtpRequest request) {

        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        PendingRegistration pending = pendingRegistrations.get(email);

        if (pending == null) {
            return ResponseEntity.badRequest()
                    .body("Registration not found or OTP expired");
        }

        if (!otpService.verifyOtp(email, request.getOtp())) {
            return ResponseEntity.badRequest()
                    .body("Invalid or expired OTP");
        }

        if (userRepo.findByEmail(email).isPresent()) {
            pendingRegistrations.remove(email);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(pending.getUsername());
        newUser.setEmail(pending.getEmail());
        newUser.setPassword(pending.getEncodedPassword());
        newUser.setRole(Role.USER);

        userRepo.save(newUser);
        pendingRegistrations.remove(email);

        return ResponseEntity.ok("Registration successful! Please login.");
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

    public UserService(
            UserRepository userRepo,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            EmailService emailService,
            OtpService otpService) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.otpService = otpService;
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



    public ResponseEntity<UserResponse> updateUser(
            Long id,
            UpdateUserRequest request) {

        User existing = userRepo.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id " + id
                        )
                );

        existing.setUsername(request.getUsername());
        existing.setEmail(request.getEmail());

        existing.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User updatedUser = userRepo.save(existing);

        UserResponse response = new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail()
        );

        return ResponseEntity.ok(response);
    }


    // ================= UPDATE USER =================



    public ResponseEntity<String> deleteUserById(Long id) {

        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException(
                    "User not found with id " + id
            );
        }

        userRepo.deleteById(id);

        return ResponseEntity.ok("User deleted successfully...!!!!!");
    }
}