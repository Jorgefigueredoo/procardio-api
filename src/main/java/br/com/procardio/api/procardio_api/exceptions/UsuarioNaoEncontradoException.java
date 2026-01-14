package br.com.procardio.api.procardio_api.exceptions;


public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com ID: " + id);
    }
    
}
