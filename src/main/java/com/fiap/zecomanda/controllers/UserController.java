package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.common.consts.UserRole;
import com.fiap.zecomanda.common.consts.UserType;
import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.*;
import com.fiap.zecomanda.entities.Address;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import com.fiap.zecomanda.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/user")
@Tag(name = "User", description = "CRUD Controller for Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDTO data) {

        if (userRepository.findByLoginAndPassword(data.login(), data.password()).isPresent()) {
            return ResponseEntity.status(409).body("User already exists.");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(data.password());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String updatedAt = LocalDateTime.now().format(formatter);

        UserRole typeUserRole = UserRole.USER;
        Address address = data.address();

        User newUser = new User(address, UserType.CUSTOMER, data.name(), data.email(), data.password(), encodedPassword, updatedAt,
                data.login(), typeUserRole);

        userService.registerUser(newUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authBody) {
        User user = this.userRepository.findByLogin(authBody.login())
                .orElse(null);
        if (user != null && passwordEncoder.matches(authBody.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok().body(new LoginResponseDTO(token, "Login succeeded."));
        }
        return ResponseEntity.status(401).body("Invalid credentials. Failed to login.");
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        var allUsers = this.userService.findAllUsers(page, size).getContent();
        return ResponseEntity.ok(allUsers);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDTO passwordDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<User> foundUser = userService.extractUserSubject(authorizationHeader);

        if (passwordDTO.currentPassword() == null || passwordDTO.currentPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Password is required.");
        }

        if (passwordDTO.newPassword() == null || passwordDTO.newPassword().isBlank()) {
            return ResponseEntity.badRequest().body("New password is required.");
        }

        if (!passwordDTO.newPassword().equals(passwordDTO.confirmationPassword())) {
            return ResponseEntity.badRequest().body("Password confirmation does not match.");
        }

        try {
            userService.updatePassword(foundUser.get().getId(), passwordDTO);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(
            @RequestBody UpdateUserDTO user,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<User> foundUser = userService.extractUserSubject(authorizationHeader);
        this.userService.updateUser(user, foundUser.get().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<User> foundUser = userService.extractUserSubject(authorizationHeader);
        this.userService.delete(foundUser.get().getId());
        return ResponseEntity.ok().build();
    }
}
