package com.fiap.techchallenge.service;

import com.fiap.techchallenge.dto.AtualizarUsuarioDto;
import com.fiap.techchallenge.dto.TrocarSenhaDto;
import com.fiap.techchallenge.model.UsuarioModel;
import com.fiap.techchallenge.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioModel cadastrarUsuario(UsuarioModel usuarioModel, String login, String senha) {
        return usuarioRepository.save(usuarioModel);
    }

    public UsuarioModel atualizarUsuario(Long id, AtualizarUsuarioDto atualizarDTO) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow();

        usuarioModel.setEmail(atualizarDTO.email());
        usuarioModel.setLogin(atualizarDTO.login());
        usuarioModel.setTelefone(atualizarDTO.telefone());
        usuarioModel.setEnderecoModel(atualizarDTO.enderecoModel());

        return usuarioRepository.save(usuarioModel);
    }

    public void trocarSenha(Long id, TrocarSenhaDto senhaDTO) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow();
        usuarioModel.setSenha(passwordEncoder.encode(senhaDTO.novaSenha()));
        usuarioRepository.save(usuarioModel);
    }

}
