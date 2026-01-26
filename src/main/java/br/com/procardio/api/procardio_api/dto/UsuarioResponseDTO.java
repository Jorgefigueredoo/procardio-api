package br.com.procardio.api.procardio_api.dto;

import br.com.procardio.api.procardio_api.model.Usuario;

public record UsuarioResponseDTO (
    
    Long id, 
    String nome, 
    String email) {
    
    public UsuarioResponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());   
    }
}
