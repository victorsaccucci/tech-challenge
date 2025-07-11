package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.model.EnderecoModel;

public record CadastroUsuarioDto(

        EnderecoModel enderecoModel,
        String nome,
        String email,
        String telefone,
        String senha,
        String login
) {
}
