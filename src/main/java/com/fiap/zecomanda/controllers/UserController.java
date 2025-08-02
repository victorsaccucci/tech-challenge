package com.fiap.zecomanda.controllers;

import com.fiap.zecomanda.common.config.swagger.openapi.controller.UserApi;
import com.fiap.zecomanda.common.config.swagger.openapi.dto.UserDtoApi;
import com.fiap.zecomanda.dto.UpdateUserDTO;
import com.fiap.zecomanda.entities.User;
import com.fiap.zecomanda.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    public ResponseEntity<List<UserDtoApi>> getUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
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
