package br.com.procardio.api.procardio_api.dto;

import java.util.Set;

import br.com.procardio.api.procardio_api.enums.Perfil;
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
    String complemento,
    Set<Perfil> perfis
) {
}