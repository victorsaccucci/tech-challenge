package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.common.config.swagger.openapi.controller.UserApi;
import com.fiap.zecomanda.common.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.dtos.UpdateUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            userService.checkUserRoleAdmin(authorizationHeader);
            List<UserDtoApi> usuarios = userService.findAllUsers();
            return ResponseEntity.ok(usuarios);

        } catch (ResponseStatusException ex) {
            Map<String, String> erro = new HashMap<>();
            erro.put("error", ex.getReason());
            return ResponseEntity.status(ex.getStatusCode()).body(erro);
        }
    }

    public ResponseEntity<Void> updateUser(
            @RequestBody UpdateUserDTO user,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<User> foundUser = userService.extractUserSubject(authorizationHeader);
        this.userService.updateUser(user, foundUser.get().getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<User> foundUser = userService.extractUserSubject(authorizationHeader);
        this.userService.delete(foundUser.get().getId());
        return ResponseEntity.ok().build();
    }
}
