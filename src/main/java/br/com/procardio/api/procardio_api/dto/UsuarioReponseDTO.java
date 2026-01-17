package br.com.procardio.api.procardio_api.dto;

import br.com.procardio.api.procardio_api.model.Usuario;

public record UsuarioReponseDTO (
    
    Long id, 
    String nome, 
    String email) {
    
    public UsuarioReponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());   
    }
}
