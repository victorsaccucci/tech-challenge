package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.model.Endereco;

public record AtualizarUsuarioDTO(
        Endereco endereco,
        String email,
        String telefone,
        String login
) {

}
