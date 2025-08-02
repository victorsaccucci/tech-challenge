package com.fiap.zecomanda.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordDTO(

        @Schema(example = "senhaAtual")
        String currentPassword,

        @Schema(example = "novaSenha")
        String newPassword,

        @Schema(example = "novaSenha")
        String confirmationPassword) {
}
