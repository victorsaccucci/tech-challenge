package com.fiap.zecomanda.service;

import com.fiap.zecomanda.common.security.TokenService;
import com.fiap.zecomanda.dto.TrocarSenhaDto;
import com.fiap.zecomanda.entity.Usuario;
import com.fiap.zecomanda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public Usuario cadastrarUsuario(Usuario usuario, String login, String senha) {
        return usuarioRepository.save(usuario);
    }

    public void trocarSenha(Long id, TrocarSenhaDto senhaDTO) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        boolean senhaAtualCorreta = passwordEncoder.matches(senhaDTO.senhaAtual(), usuario.getSenha());
        
        if (!senhaAtualCorreta) {
        throw new IllegalArgumentException("A senha atual está incorreta.");
    }
        usuario.setSenha(passwordEncoder.encode(senhaDTO.novaSenha()));
        usuarioRepository.save(usuario);
    }

    public void updateUsuario(Usuario usuario, Long id) {
        var update = this.usuarioRepository.update(usuario, id);
        if (update == 0) {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public void delete(Long id) {
        var delete = this.usuarioRepository.delete(id);
        if (delete == 0) {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario extrairUsuarioDoToken(String token){
        String subjectLogin = tokenService.extractSubject(token);
        Usuario usuarioEncontrado = usuarioRepository.encontrarUsuarioPorLogin(subjectLogin);
        return usuarioEncontrado;
    }
}
