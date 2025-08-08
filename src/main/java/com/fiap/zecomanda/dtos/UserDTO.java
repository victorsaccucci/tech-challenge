package com.fiap.zecomanda.dtos;

import com.fiap.zecomanda.commons.consts.UserType;
import com.fiap.zecomanda.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserDTO(

        @Schema(example = "João Silva")
        @NotBlank
        String name,

        @Schema(example = "joao.silva@fiap.com")
        @NotBlank
        @Email
        String email,

        @Schema(example = "+55 11 91234-5678")
        @NotBlank
        String phoneNumber,

        @Schema(example = "joaosilva")
        @NotBlank
        String login,

        @Schema(example = "12345")
        @NotBlank
        String password,

        UserType userType,

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
        @Valid
        Address address
) {
}
