package br.com.procardio.api.procardio_api.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioDTO {
    
    @NotBlank
    String nome;
    @NotBlank
    String email;
    @NotBlank   
    String senha;
    @NotBlank
    String Cep;
    @NotBlank
    String Complemento;
}
