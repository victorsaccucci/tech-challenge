package com.fiap.zecomanda.services;

import com.fiap.zecomanda.commons.consts.UserRole;
import com.fiap.zecomanda.commons.consts.UserType;
import com.fiap.zecomanda.commons.security.TokenService;
import com.fiap.zecomanda.commons.validations.EmailUnique;
import com.fiap.zecomanda.commons.validations.LoginUnique;
import com.fiap.zecomanda.dtos.AuthenticationDTO;
import com.fiap.zecomanda.dtos.ChangePasswordDTO;
import com.fiap.zecomanda.dtos.LoginResponseDTO;
import com.fiap.zecomanda.dtos.RequestUserDTO;
import com.fiap.zecomanda.entities.Address;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.repositories.AddressRepository;
import com.fiap.zecomanda.repositories.UserRepository;
import com.fiap.zecomanda.commons.exceptions.UnauthorizedAccessException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void registerUser(@Valid RequestUserDTO data) {

        // Validações de entrada do DTO já rodaram aqui (@NotBlank, @Email, etc.)

        // Regras de negócio “de verdade” ficam aqui (ex.: política de senha, limites, etc.)
        // if (!policyOk(data.password())) throw new OperationNotAllowedException(...);

        String encodedPassword = passwordEncoder.encode(data.password());
        LocalDateTime updatedAt = LocalDateTime.now();

        // se vier sem id, salva; se vier com id, anexa a gerenciada
        Address managedAddress = (data.address().getId() == null)
                ? addressRepository.save(data.address())
                : addressRepository.getReferenceById(data.address().getId());

        // Aqui entram as validações do nosso amigo Mychel (únicas) NO SERVICE:


        createUserWithValidatedParams(
                managedAddress,
                data.name(),
                data.email(),   // @EmailUnique roda aqui
                data.phoneNumber(),
                encodedPassword,
                updatedAt,
                data.login()   // @LoginUnique roda aqui
        );
    }

    void createUserWithValidatedParams(
            Address address,
            String name,
            @EmailUnique String email,
            String phoneNumber,
            String encodedPassword,
            LocalDateTime updatedAt,
            @LoginUnique String login
    ) {
        User newUser = new User(
                address,
                UserType.CUSTOMER,
                name,
                email,
                phoneNumber,
                encodedPassword,
                updatedAt,
                login,
                UserRole.USER
        );

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
