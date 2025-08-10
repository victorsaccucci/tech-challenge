package com.fiap.zecomanda.services;

import com.fiap.zecomanda.commons.exceptions.AccessDeniedException;
import com.fiap.zecomanda.commons.exceptions.ResourceAlreadyExistsException;
import com.fiap.zecomanda.commons.security.TokenService;
import com.fiap.zecomanda.dtos.*;
import com.fiap.zecomanda.entities.Address;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.AddressRepository;
import com.fiap.zecomanda.repositories.UserRepository;
import com.fiap.zecomanda.commons.exceptions.UnauthorizedAccessException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Validated
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // Regra simples para segurança de senha ao trocar: mínimo 8, ao menos 1 letra e 1 número
    private static final Pattern PWD_POLICY = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    private void ensurePasswordPolicy(String newPassword) {
        if (newPassword == null || !PWD_POLICY.matcher(newPassword).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters and include letters and numbers.");
        }
    }

    public boolean resourceExists(String login, String email) {
        return userRepository.findByLogin(login).isPresent() || userRepository.findByEmail(email).isPresent();
    }

    public void registerUser(UserDTO userDTO) {
        if (resourceExists(userDTO.login(), userDTO.email())) {
            throw new ResourceAlreadyExistsException("Login or email already exists.");
        }

        Address address = (userDTO.address().getId() == null)
                ? addressRepository.save(userDTO.address())
                : addressRepository.getReferenceById(userDTO.address().getId());

        var newUser = User.builder().name(userDTO.name()).email(userDTO.email()).phoneNumber(userDTO.phoneNumber())
                .login(userDTO.login()).password(passwordEncoder.encode(userDTO.password())).updatedAt(LocalDateTime.now())
                .userType(userDTO.userType()).address(address).build();

        userRepository.save(newUser);
    }

    public LoginResponseDTO login(AuthenticationDTO authBody) {
        User user = this.userRepository.findByLogin(authBody.login())
                .orElseThrow(() -> new UnauthorizedAccessException("Invalid login or password."));

        if (passwordEncoder.matches(authBody.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return new LoginResponseDTO(token, "Login succeeded.");
        }

        throw new UnauthorizedAccessException("Invalid login or password.");
    }

    public void changePassword(ChangePasswordDTO passwordDTO, String tokenHeader) {
        if (!passwordDTO.newPassword().equals(passwordDTO.confirmationPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match.");
        }

        ensurePasswordPolicy(passwordDTO.newPassword());

        User user = extractUserSubject(tokenHeader)
                .orElseThrow(() -> new UnauthorizedAccessException("Invalid login or password."));

        if (!passwordEncoder.matches(passwordDTO.currentPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid login or password.");
        }

        if (passwordEncoder.matches(passwordDTO.newPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        userRepository.save(user);
    }

    public Optional<User> extractUserSubject(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new UnauthorizedAccessException("Invalid login or password.");
        }

        try {
            String token = tokenHeader.substring(7);
            String subjectLogin = tokenService.extractSubject(token);
            Optional<User> user = userRepository.findById(Long.valueOf(subjectLogin));

            if (user.isEmpty()) {
                throw new AccessDeniedException("Access is denied."); // 403
            }
            return user;
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Invalid login or password."); // 401
        }
    }
}
