package com.fiap.zecomanda.controller;

import com.fiap.zecomanda.common.consts.EnumType;
import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.*;
import com.fiap.zecomanda.entity.Address;
import com.fiap.zecomanda.entity.User;
import com.fiap.zecomanda.common.consts.UserRole;
import com.fiap.zecomanda.repository.UserRepository;
import com.fiap.zecomanda.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("api/v1/user")
@Tag(name = "User", description = "CRUD Controller for Users")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid RegisterUserDTO data) {

        if (!this.userRepository.findByEmail(data.email()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String senhaCodificado = new BCryptPasswordEncoder().encode(data.password());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dtUltimaAtualizacao = LocalDateTime.now().format(formatter);

        UserRole cargo = UserRole.USER;
        Address address = data.address();

        User novoUser = new User(address, EnumType.CLIENTE, data.name(), data.email(), data.password(), senhaCodificado, dtUltimaAtualizacao,
                data.login(), cargo);

        usuarioService.registerUser(novoUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO body) {
        User user = this.userRepository.findByLogin(body.login()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> listarUsuarios() {
        var usuarios = this.usuarioService.findAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> trocarSenha(
            @RequestBody ChangePasswordDTO senhaDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        User userEncontrado = usuarioService.extractUserSubject(authorizationHeader);

        if (senhaDTO.currentPassword() == null || senhaDTO.currentPassword().isBlank()) {
            return ResponseEntity.badRequest().body("A senha atual é obrigatória.");
        }

        if (senhaDTO.newPassword() == null || senhaDTO.newPassword().isBlank()) {
            return ResponseEntity.badRequest().body("A nova senha é obrigatória.");
        }

        if (!senhaDTO.newPassword().equals(senhaDTO.confirmationPassword())) {
            return ResponseEntity.badRequest().body("As senhas não coincidem");
        }

        try {
            usuarioService.updatePassword(userEncontrado.getId(), senhaDTO);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVeiculo(@RequestBody User user, @PathVariable("id") Long id) {
        this.usuarioService.updateUser(user, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable("id") Long id) {
         this.usuarioService.delete(id);
        return ResponseEntity.ok().build();
    }
}
