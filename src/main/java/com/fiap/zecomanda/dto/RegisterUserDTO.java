package com.fiap.zecomanda.dto;
import com.fiap.zecomanda.entity.Address;

public record RegisterUserDTO(
        Address address,
        String name,
        String email,
        String phoneNumber,
        String password,
        String login
) {
}
