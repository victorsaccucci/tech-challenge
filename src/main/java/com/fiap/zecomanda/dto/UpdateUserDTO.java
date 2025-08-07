package com.fiap.zecomanda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserDTO(
        @Schema(example = "João Silva")
        @NotEmpty
        String name,

        @Schema(example = "joao.silva@fiap.com")
        @NotEmpty
        String email,

        @Schema(example = "+55 11 91234-5678")
        @NotEmpty
        String phoneNumber,

        @Schema(example = "joao")
        @NotEmpty
        String login
) {

}
