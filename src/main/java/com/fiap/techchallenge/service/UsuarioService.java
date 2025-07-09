package com.fiap.techchallenge.service;

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

}
