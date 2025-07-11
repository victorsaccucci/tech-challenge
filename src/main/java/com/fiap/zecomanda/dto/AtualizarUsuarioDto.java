package com.fiap.zecomanda.dto;

import com.fiap.zecomanda.model.Endereco;

public record AtualizarUsuarioDTO(       
        Endereco endereco,
        String email,
        String telefone,
        String login
) {

}
