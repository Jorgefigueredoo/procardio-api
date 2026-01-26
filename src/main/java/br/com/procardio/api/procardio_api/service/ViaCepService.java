package br.com.procardio.api.procardio_api.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.procardio.api.procardio_api.dto.EnderecoDTO;

@Service
public class ViaCepService {

    private final String URL_BASE_VIACEP = "https://viacep.com.br/ws/";

    public EnderecoDTO obterDadosEnderecoPeloCep(String cep) {
        if (Objects.isNull(cep) || cep.isBlank()) {
            return null;
        }

        String cepFormatado = cep.replace("\\D", "");

        if (cepFormatado.length() != 8) {
            throw new IllegalArgumentException("CEP inválido!");
        }

        StringBuilder sb = new StringBuilder();

        String url = sb.append(URL_BASE_VIACEP)
                .append(cepFormatado)
                .append("/json")
                .toString();

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.getForObject(url, EnderecoDTO.class);
        } catch (Exception ex) {
            System.err.println("Erro ao consultar o ViaCEP: " + ex.getMessage());
            return null;
        }
    }
}
