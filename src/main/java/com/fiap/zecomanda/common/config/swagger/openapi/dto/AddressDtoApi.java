package com.fiap.zecomanda.common.config.swagger.openapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Endereço do usuário")
public record AddressDtoApi(

        @Schema(example = "Rua das Flores")
        String street,

        @Schema(example = "Centro")
        String neighborhood,

        @Schema(example = "São Paulo")
        String city,

        @Schema(example = "123")
        String number,

        @Schema(example = "SP")
        String state,

        @Schema(example = "Brasil")
        String country
) {
}