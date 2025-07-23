package com.fiap.zecomanda.dto;

public record ChangePasswordDTO(
        String currentPassword,
        String newPassword,
        String confirmationPassword) {
}
