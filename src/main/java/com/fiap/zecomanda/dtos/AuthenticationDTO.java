package com.fiap.zecomanda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationDTO(

        @Schema(example = "joao")
        String login,

        @Schema(example = "123")
        String password) {
}
