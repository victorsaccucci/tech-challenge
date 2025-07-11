package com.fiap.zecomanda.controller;

import com.fiap.zecomanda.common.consts.TipoEnum;
import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.*;
import com.fiap.zecomanda.entity.Endereco;
import com.fiap.zecomanda.entity.Usuario;
import com.fiap.zecomanda.common.consts.UsuarioCargoEnum;
import com.fiap.zecomanda.repository.UsuarioRepository;
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
@RequestMapping("api/v1/usuario")
@Tag(name = "Usuarios", description = "Controller para crud de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDto data) {

        if (!this.usuarioRepository.findByEmail(data.email()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String senhaCodificado = new BCryptPasswordEncoder().encode(data.senha());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dtUltimaAtualizacao = LocalDateTime.now().format(formatter);

        UsuarioCargoEnum cargo = UsuarioCargoEnum.USER;
        Endereco endereco = data.endereco();

        Usuario novoUsuario = new Usuario(endereco, TipoEnum.CLIENTE, data.nome(), data.email(), data.telefone(), senhaCodificado, dtUltimaAtualizacao,
                data.login(), cargo);

        String login = data.login();
        String senha = data.senha();

        usuarioService.cadastrarUsuario(novoUsuario, login, senha);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AutenticacaoDto body) {
        Usuario user = this.usuarioRepository.findByLogin(body.login()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.senha(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginRespostaDto(token));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        var usuarios = this.usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<?> trocarSenha(@PathVariable Long id, @RequestBody TrocarSenhaDto senhaDTO) {
        String novaSenha = senhaDTO.novaSenha();

        if (novaSenha == null || novaSenha.isBlank()) {
            return ResponseEntity.badRequest().body("A nova senha não pode estar vazia.");
        }
        usuarioService.trocarSenha(id, senhaDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVeiculo(@RequestBody Usuario usuario, @PathVariable("id") Long id) {
        this.usuarioService.updateUsuario(usuario, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable("id") Long id) {
         this.usuarioService.delete(id);
        return ResponseEntity.ok().build();
    }
}
