package com.fiap.zecomanda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserDTO(
        @Schema(example = "João Silva")
        @NotBlank
        String name,

        @Schema(example = "joao.silva@fiap.com")
        @NotBlank
        String email,

        @Schema(example = "+55 11 91234-5678")
        @NotBlank
        String phoneNumber,

        @Schema(example = "joao")
        @NotBlank
        String login
) {

}
