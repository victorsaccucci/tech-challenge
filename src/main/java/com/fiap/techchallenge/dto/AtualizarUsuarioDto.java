package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.model.EnderecoModel;

public record AtualizarUsuarioDto(

        EnderecoModel enderecoModel,
        String email,
        String telefone,
        String login
) {

}
