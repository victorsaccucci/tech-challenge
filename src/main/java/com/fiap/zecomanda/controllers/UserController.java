package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.commons.config.swagger.openapi.controller.UserApi;
import com.fiap.zecomanda.commons.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.dtos.DeleteUserDTO;
import com.fiap.zecomanda.dtos.UpdateUserDTO;
import com.fiap.zecomanda.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authorizationHeader) {
        // userService.checkUserRoleAdmin(authorizationHeader); // se for usar roles
        List<UserDtoApi> usuarios = userService.findAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @Override
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserDTO user,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        userService.updateUser(user);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DeleteUserDTO> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id
    ) {
        var resp = userService.delete(id);
        return ResponseEntity.ok(resp);
    }
}
