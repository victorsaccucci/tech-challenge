package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.entity.Endereco;

public record AtualizarUsuarioDto(

        Endereco endereco,
        String email,
        String telefone,
        String login
) {

}
