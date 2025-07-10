package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.model.Endereco;

public record CadastroUsuarioDTO(
        Endereco endereco,
        String nome,
        String email,
        String telefone,
        String senha,
        String login
) {
}
