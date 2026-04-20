package com.empresa.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
        int maxRetries = 3;
        int attempt = 0;
        long backoff = 500; // milissegundos
        IOException lastException = null;

        while (attempt < maxRetries) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(3))
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
            } catch (IOException | InterruptedException e) {
                attempt++;
                lastException = e instanceof IOException ? (IOException) e : new IOException(e);
                if (attempt >= maxRetries) throw lastException;
                Thread.sleep(backoff * attempt); // backoff exponencial simples
            }
        }
        throw lastException;
    }
}
