package com.fiap.zecomanda.dto;

import com.fiap.zecomanda.entity.Endereco;

public record AtualizarUsuarioDto(
        Endereco endereco,
        String email,
        String telefone,
        String login
) {

}
