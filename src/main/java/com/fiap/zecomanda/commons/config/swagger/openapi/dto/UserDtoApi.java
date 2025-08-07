package com.fiap.zecomanda.commons.config.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário")
public record UserDtoApi(

        @Schema(example = "1")
        Long id,

        @Schema(example = "joaosilva")
        String login,

        @Schema(example = "João Silva")
        String name,

        @Schema(example = "joao@email.com")
        String email,

        @Schema(example = "11999999999")
        String phoneNumber,


        @Schema(example = "29-07-2025")
        String updatedAt,


        @Schema(description = "Endereço do usuário")
        AddressDtoApi address
) {
}
