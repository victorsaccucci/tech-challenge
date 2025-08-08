package com.fiap.zecomanda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(

        @Schema(example = "joao")
        @NotBlank
        String login,

        @Schema(example = "123")
        @NotBlank
        String password) {
}
