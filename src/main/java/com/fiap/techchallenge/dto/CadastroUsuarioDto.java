package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.entity.Endereco;

public record CadastroUsuarioDto(

        Endereco endereco,
        String nome,
        String email,
        String telefone,
        String senha,
        String login
) {
}
