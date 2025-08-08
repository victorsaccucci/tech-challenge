package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.commons.config.swagger.openapi.controller.AuthApi;
import com.fiap.zecomanda.dtos.AuthenticationDTO;
import com.fiap.zecomanda.dtos.ChangePasswordDTO;
import com.fiap.zecomanda.dtos.LoginResponseDTO;
import com.fiap.zecomanda.dtos.UserDTO;
import com.fiap.zecomanda.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO) {
        authService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO authBody) {
        LoginResponseDTO response = authService.login(authBody);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordDTO passwordDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            authService.changePassword(passwordDTO, authorizationHeader);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
