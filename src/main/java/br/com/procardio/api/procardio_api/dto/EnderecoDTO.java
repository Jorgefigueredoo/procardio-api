package br.com.procardio.api.procardio_api.dto;

public record EnderecoDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        boolean erro) {

}
