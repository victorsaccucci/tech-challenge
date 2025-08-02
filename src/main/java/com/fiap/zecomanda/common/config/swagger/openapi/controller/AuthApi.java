package com.fiap.zecomanda.common.config.swagger.openapi.controller;

import com.fiap.zecomanda.dto.AuthenticationDTO;
import com.fiap.zecomanda.dto.ChangePasswordDTO;
import com.fiap.zecomanda.dto.RequestUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/auth")
@Tag(name = "Autenticação")
public interface AuthApi {

    @Operation(summary = "Cadastrar um novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })
    @PostMapping("/register")
    ResponseEntity<?> registerUser(
            @Parameter(description = "Dados do novo usuário", required = true)
            @Valid RequestUserDTO data
    );

    @Operation(summary = "Login de usuário", description = "Autentica o usuário e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    ResponseEntity<?> login(
            @Parameter(description = "Credenciais de autenticação do usuário", required = true)
            AuthenticationDTO authBody
    );


    @Operation(summary = "Alterar senha do usuário", description = "Altera a senha do usuário com um token JWT.")
    @PatchMapping("/change-password")
    ResponseEntity<?> changePassword(
            @RequestBody
            ChangePasswordDTO passwordDTO,
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );
}
