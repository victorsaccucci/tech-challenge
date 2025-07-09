package com.fiap.techchallenge.controller;

import com.fiap.techchallenge.Enum.Tipo;
import com.fiap.techchallenge.dto.AutenticacaoDTO;
import com.fiap.techchallenge.dto.CadastroUsuarioDTO;
import com.fiap.techchallenge.dto.LoginRespostaDTO;
import com.fiap.techchallenge.infra.security.TokenService;
import com.fiap.techchallenge.model.Endereco;
import com.fiap.techchallenge.model.Usuario;
import com.fiap.techchallenge.model.UsuarioCargo;
import com.fiap.techchallenge.repository.UsuarioRepository;
import com.fiap.techchallenge.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/v1/usuario")
@Tag(name = "Usuarios", description = "Controller para crud de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDTO data) {

        if (!this.usuarioRepository.findByEmail(data.email()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String senhaCodificado = new BCryptPasswordEncoder().encode(data.senha());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dtUltimaAtualizacao = LocalDateTime.now().format(formatter);

        UsuarioCargo cargo = UsuarioCargo.USER;
        Endereco endereco = data.endereco();
        Tipo tipo = Tipo.CLIENTE;

        Usuario novoUsuario = new Usuario(endereco, tipo, data.nome(), data.email(), data.telefone(), senhaCodificado, dtUltimaAtualizacao,
                data.login(), cargo);

        String login = data.login();
        String senha = data.senha();

        usuarioService.cadastrarUsuario(novoUsuario, login, senha);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AutenticacaoDTO body){
        Usuario user = this.usuarioRepository.findByLogin(body.login()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.senha(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginRespostaDTO(token));
        }
        return ResponseEntity.badRequest().build();
    }

}
