package com.fiap.zecomanda.dto;

import com.fiap.zecomanda.entities.Address;
import com.fiap.zecomanda.validations.EmailUnique;
import com.fiap.zecomanda.validations.LoginUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestUserDTO(

        @Schema(example = "João Silva")
        @NotNull
        @Size(min = 3)
        String name,

        @Schema(example = "joao.silva@fiap.com")
        @NotNull
        @Size(min = 3)
        @EmailUnique
        String email,

        @Schema(example = "+55 11 91234-5678")
        @NotNull
        @Size(min = 3)
        String phoneNumber,

        @Schema(example = "joao")
        @NotNull
        @Size(min = 3)
        @LoginUnique
        String login,

        @Schema(example = "123")
        @NotNull
        @Size(min = 3)
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
        @NotNull
        Address address
) {
}
