package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.common.config.swagger.openapi.controller.AuthApi;
import com.fiap.zecomanda.dto.AuthenticationDTO;
import com.fiap.zecomanda.dto.ChangePasswordDTO;
import com.fiap.zecomanda.dto.LoginResponseDTO;
import com.fiap.zecomanda.dto.RequestUserDTO;
import com.fiap.zecomanda.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;


    public ResponseEntity<?> registerUser(@RequestBody RequestUserDTO data) {
        try {
            authService.registerUser(data);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authBody) {
        try {
            LoginResponseDTO response = authService.login(authBody);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((e.getMessage()));
        }
    }

    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDTO passwordDTO,
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
