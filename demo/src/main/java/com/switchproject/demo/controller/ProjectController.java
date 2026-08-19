    package com.switchproject.demo.controller;

    import com.switchproject.demo.dto.*;
    import com.switchproject.demo.model.User;
    import com.switchproject.demo.service.UserService;
    import org.apache.coyote.Response;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import java.util.List;
    import java.util.Optional;
    import com.switchproject.demo.dto.UpdateUserRequest;
    import com.switchproject.demo.dto.UserResponse;
    import jakarta.validation.Valid;
    import jakarta.validation.Valid;
    import com.switchproject.demo.dto.UpdateUserRequest;
    import jakarta.validation.Valid;

    @RestController
    public class ProjectController {


        private final UserService userService;

        public ProjectController(UserService userService){
            this.userService = userService;
        }


        @PostMapping("/register")
        public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
            return userService.register(request);
        }

        @PutMapping("/user/{id}")
        public ResponseEntity<UserResponse> updateUser(
                @PathVariable Long id,
                @Valid @RequestBody UpdateUserRequest request) {

            return userService.updateUser(id, request);
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> userlogin(@RequestBody LoginRequest request){
            return userService.login(request);
        }

        @GetMapping("/users/all")
        public ResponseEntity<List<UserResponse>> getAllUsers(){
            return userService.allUsers();
        }



        @GetMapping("/user/{id}")
        public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
            return userService.getUserById(id);
        }

        @DeleteMapping("/delete/user/{id}")
        public ResponseEntity<String> deleteUserById(@PathVariable Long id){
            return userService.deleteUserById(id);
        }


    }
