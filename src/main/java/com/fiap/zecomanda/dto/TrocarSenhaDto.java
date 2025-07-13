package com.fiap.zecomanda.dto;

public record TrocarSenhaDto(
    String senhaAtual, 
    String novaSenha, 
    String confirmacaoSenha) {
}
