package com.fiap.zecomanda.commons.config.swagger.openapi.dto;

import com.fiap.zecomanda.commons.consts.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do usuário")
public record UserDtoApi(

        @Schema(example = "1")
        Long id,

        @Schema(example = "João Silva")
        String name,

        @Schema(example = "joao@email.com")
        String email,

        @Schema(example = "11999999999")
        String phoneNumber,

        @Schema(example = "COSTUMER")
        UserType userType,

        @Schema(example = "joaosilva")
        String login,

        @Schema(example = "29-07-2025")
        String updatedAt,

        @Schema(example = "false")
        Boolean deleted,

        @Schema(description = "Endereço do usuário")
        AddressDtoApi address
) {}
