package com.fiap.zecomanda.dto;
import com.fiap.zecomanda.model.Endereco;

public record CadastroUsuarioDTO(
        Endereco endereco,
        String nome,
        String email,
        String telefone,
        String senha,
        String login
) {
}
