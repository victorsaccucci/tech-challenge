package com.fiap.zecomanda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserDTO(
        @Schema(example = "João Silva")
        String name,

        @Schema(example = "joao.silva@fiap.com")
        String email,

        @Schema(example = "+55 11 91234-5678")
        String phoneNumber,

        @Schema(example = "joao")
        String login
) {

}
