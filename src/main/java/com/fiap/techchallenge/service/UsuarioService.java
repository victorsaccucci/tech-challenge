package com.fiap.techchallenge.service;

import com.fiap.techchallenge.dto.AtualizarUsuarioDTO;
import com.fiap.techchallenge.model.Usuario;
import com.fiap.techchallenge.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario, String login, String senha){
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioDTO atualizarDTO){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        
        usuario.setEmail(atualizarDTO.email());
        usuario.setLogin(atualizarDTO.login());
        usuario.setTelefone(atualizarDTO.telefone());
        usuario.setEndereco(atualizarDTO.endereco());

        return usuarioRepository.save(usuario);
    }

}
