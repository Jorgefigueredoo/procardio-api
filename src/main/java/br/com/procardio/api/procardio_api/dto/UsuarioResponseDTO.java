package br.com.procardio.api.procardio_api.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        EnderecoDTO endereco
) {

}