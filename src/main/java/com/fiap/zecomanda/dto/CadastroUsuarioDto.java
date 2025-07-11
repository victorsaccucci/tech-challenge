package com.fiap.zecomanda.dto;
import com.fiap.zecomanda.entity.Endereco;

public record CadastroUsuarioDto(
        Endereco endereco,
        String nome,
        String email,
        String telefone,
        String senha,
        String login
) {
}
