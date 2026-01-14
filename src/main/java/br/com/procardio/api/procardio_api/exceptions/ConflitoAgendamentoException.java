package br.com.procardio.api.procardio_api.exceptions;

public class ConflitoAgendamentoException extends RuntimeException {

    public ConflitoAgendamentoException(String mensagem) {
        super(mensagem);
    }

}