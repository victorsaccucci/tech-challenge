package com.fiap.zecomanda.services;

import com.fiap.zecomanda.common.consts.UserRole;
import com.fiap.zecomanda.common.consts.UserType;
import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.AuthenticationDTO;
import com.fiap.zecomanda.dto.ChangePasswordDTO;
import com.fiap.zecomanda.dto.LoginResponseDTO;
import com.fiap.zecomanda.dto.RequestUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void registerUser(RequestUserDTO data) {
        String encodedPassword = passwordEncoder.encode(data.password());
        String updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        User newUser = new User(
                data.address(),
                UserType.CUSTOMER,
                data.name(),
                data.email(),
                data.phoneNumber(),
                encodedPassword,
                updatedAt,
                data.login(),
                UserRole.USER
        );

        userRepository.save(newUser);
    }

    public LoginResponseDTO login(AuthenticationDTO authBody) {
        User user = this.userRepository.findByLogin(authBody.login())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (passwordEncoder.matches(authBody.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return new LoginResponseDTO(token, "Login succeeded.");
        }
        throw new RuntimeException("Invalid credentials");
    }

    public void changePassword(ChangePasswordDTO passwordDTO, String tokenHeader) {
        if (passwordDTO.currentPassword() == null || passwordDTO.currentPassword().isBlank()) {
            throw new IllegalArgumentException("Current password is required.");
        }

        if (passwordDTO.newPassword() == null || passwordDTO.newPassword().isBlank()) {
            throw new IllegalArgumentException("New password is required.");
        }

        if (!passwordDTO.newPassword().equals(passwordDTO.confirmationPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match.");
        }

        User user = extractUserSubject(tokenHeader)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        boolean validPassword = passwordEncoder.matches(passwordDTO.currentPassword(), user.getPassword());

        if (!validPassword) {
            throw new IllegalArgumentException("The current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        userRepository.save(user);
    }

    public Optional<User> extractUserSubject(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing token.");
        }

        try {
            String token = tokenHeader.substring(7);
            String subjectLogin = tokenService.extractSubject(token);
            return userRepository.findById(Long.valueOf(subjectLogin));
        } catch (Exception e) {
            throw new RuntimeException("Unable to authenticate user. Please log in again.");
        }
    }
}
