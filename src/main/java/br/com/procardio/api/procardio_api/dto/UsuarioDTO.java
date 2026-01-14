package br.com.procardio.api.procardio_api.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String nome, 
    @NotBlank
    String email,
    @NotBlank
    String senha,
    String cep,
    String numero,
    String complemento
) {
}