package com.fiap.zecomanda.service;

import com.fiap.zecomanda.dto.AtualizarUsuarioDto;
import com.fiap.zecomanda.dto.TrocarSenhaDto;
import com.fiap.zecomanda.entity.Usuario;
import com.fiap.zecomanda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario, String login, String senha) {
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioDto atualizarDTO) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        usuario.setEmail(atualizarDTO.email());
        usuario.setLogin(atualizarDTO.login());
        usuario.setTelefone(atualizarDTO.telefone());
        usuario.setEndereco(atualizarDTO.endereco());

        return usuarioRepository.save(usuario);
    }

    public void trocarSenha(Long id, TrocarSenhaDto senhaDTO) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setSenha(passwordEncoder.encode(senhaDTO.novaSenha()));
        usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuarioRepository.delete(usuario);
    }
}
