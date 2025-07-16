package com.fiap.zecomanda.dto;

import com.fiap.zecomanda.entity.Address;

public record UpdateUserDTO(
        Address address,
        String email,
        String phoneNumber,
        String login
) {

}
