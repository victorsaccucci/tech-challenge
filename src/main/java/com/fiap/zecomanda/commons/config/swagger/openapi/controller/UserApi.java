package com.fiap.zecomanda.commons.config.swagger.openapi.controller;

import com.fiap.zecomanda.dtos.UpdateUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/user")
@Tag(name = "Usuários")
public interface UserApi {

    @Operation(summary = "Listar usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping()
    ResponseEntity<?> getUsers(
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );

    @Operation(summary = "Atualizar dados do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping()
    ResponseEntity<Void> updateUser(
            UpdateUserDTO user,
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );

    @Operation(summary = "Deletar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping()
    ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );
}
