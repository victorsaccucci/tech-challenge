package com.fiap.zecomanda.dto;

import com.fiap.zecomanda.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserDTO(

        @Schema(example = "João Silva")
        String name,

        @Schema(example = "joao.silva@fiap.com")
        String email,

        @Schema(example = "+55 11 91234-5678")
        String phoneNumber,

        @Schema(example = "joao")
        String login,

        @Schema(example = "123")
        String password,

        @Schema(example = """
                    {
                      "street": "Rua das Flores",
                      "neighborhood": "Centro",
                      "city": "São Paulo",
                      "number": "123",
                      "state": "SP",
                      "country": "Brasil"
                    }
                """)
        Address address
) {
}
