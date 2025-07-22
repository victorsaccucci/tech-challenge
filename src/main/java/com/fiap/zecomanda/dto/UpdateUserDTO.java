package com.fiap.zecomanda.dto;

public record UpdateUserDTO(
        String name,
        String email,
        String phoneNumber,
        String login
) {

}
