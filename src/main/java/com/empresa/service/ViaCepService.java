
package com.empresa.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.empresa.dto.ViaCepDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ViaCepService {
	
	private static final String BASE_URL = "https://viacep.com.br/ws/";
	private static final int TIMEOUT_SECONDS = 3; // pode ser configurado
	private static final Map<String, ViaCepDTO> cache = new ConcurrentHashMap<>();
	private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
			.build();
	private final ObjectMapper objectMapper = new ObjectMapper();

    public ViaCepDTO buscarEnderecoPorCep(String cep) throws IOException, InterruptedException {
        // Cache simples em memória
        if (cache.containsKey(cep)) {
            return cache.get(cep);
        }

        int maxRetries = 3;
        int attempt = 0;
        long backoff = 500; // milissegundos
        IOException lastException = null;
        while (attempt < maxRetries) {
            try {
                String url = BASE_URL + cep + "/json/";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    ViaCepDTO value = objectMapper.readValue(response.body(), ViaCepDTO.class);
                    cache.put(cep, value);
                    return value;
                } else {
                    throw new IOException("Erro ao consultar ViaCEP: HTTP " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                attempt++;
                lastException = e instanceof IOException ? (IOException) e : new IOException(e);
                if (attempt >= maxRetries) throw lastException;
                try {
                    Thread.sleep(backoff * attempt); // backoff exponencial simples
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IOException("Falha ao consultar ViaCEP após retentativas.");
    }
}
