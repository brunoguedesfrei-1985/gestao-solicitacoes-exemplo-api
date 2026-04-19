package com.empresa.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CpfValidatorService {
    private static final String BASE_URL = "https://scpa-backend.saude.gov.br/public/scpa-usuario/validacao-cpf/";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isCpfValido(String cpf) throws IOException, InterruptedException {
        String url = BASE_URL + cpf;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonNode json = objectMapper.readTree(response.body());
            return "true".equals(json.asText());
        } else if (response.statusCode() == 400 && "cpf-nao-encotrado-receita-federal".equals(objectMapper.readTree(response.body()).findValue("error").asText())) {
        	return false;
        }
        throw new IOException("Erro ao consultar serviço de validação de CPF: HTTP " + response.statusCode());
    }
}
