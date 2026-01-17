package br.com.procardio.api.procardio_api.dto;

import jakarta.validation.constraints.NotBlank;

// DTO para dados de login
public record LoginDTO(
    // Campos obrigatórios para login
        @NotBlank String email,
        @NotBlank String senha) {
}