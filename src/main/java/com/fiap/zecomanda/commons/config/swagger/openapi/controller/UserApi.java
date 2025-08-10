package com.fiap.zecomanda.commons.config.swagger.openapi.controller;

import com.fiap.zecomanda.dtos.DeleteUserDTO;
import com.fiap.zecomanda.dtos.UpdateUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
@Tag(name = "Usuários")
public interface UserApi {

    @Operation(summary = "Listar usuários cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content)
    })
    @GetMapping
    ResponseEntity<?> getUsers(
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );

    @Operation(summary = "Atualizar dados do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (e-mail/login duplicado)", content = @Content)
    })
    @PutMapping
    ResponseEntity<Void> updateUser(
            @RequestBody @Valid UpdateUserDTO user,
            @RequestHeader("Authorization")
            @Parameter(hidden = true)
            String authorizationHeader
    );

    @Operation(summary = "Deletar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (ex.: tentar deletar MANAGER)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (restrição de integridade/FK)", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<DeleteUserDTO> deleteUser(
            @RequestHeader("Authorization") @Parameter(hidden = true) String authorizationHeader,
            @PathVariable Long id
    );
}
