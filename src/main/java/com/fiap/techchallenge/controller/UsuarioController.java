package com.fiap.techchallenge.controller;

import com.fiap.techchallenge.common.consts.TipoEnum;
import com.fiap.techchallenge.dto.*;
import com.fiap.techchallenge.common.infra.security.TokenService;
import com.fiap.techchallenge.entity.Endereco;
import com.fiap.techchallenge.entity.Usuario;
import com.fiap.techchallenge.common.consts.UsuarioCargoEnum;
import com.fiap.techchallenge.repository.UsuarioRepository;
import com.fiap.techchallenge.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    @PutMapping("/{id}/atualizar")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody AtualizarUsuarioDto atualizarDTO) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, atualizarDTO));
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

    @DeleteMapping("/{id}/deletar")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
