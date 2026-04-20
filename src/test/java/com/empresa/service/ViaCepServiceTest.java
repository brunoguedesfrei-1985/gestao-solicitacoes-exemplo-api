package com.empresa.service;

import com.empresa.dto.ViaCepDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ViaCepServiceTest {

    @Test
    void buscarEnderecoPorCep_deveRetornarEnderecoMockado() throws Exception {
        // Arrange
        String cep = "01001000";
        String json = "{\"cep\":\"01001-000\",\"logradouro\":\"Praça da Sé\",\"bairro\":\"Sé\",\"localidade\":\"São Paulo\",\"uf\":\"SP\"}";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<Object> httpResponseMock = mock(HttpResponse.class);

        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(), any())).thenReturn(httpResponseMock);

        ViaCepService service = new ViaCepService();
        // Injetar mocks via reflexão
        var httpClientField = ViaCepService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);

        var objectMapperField = ViaCepService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        // Act
        ViaCepDTO dto = service.buscarEnderecoPorCep(cep);

        // Assert
        assertNotNull(dto);
        assertEquals("01001-000", dto.cep);
        assertEquals("Praça da Sé", dto.logradouro);
        assertEquals("São Paulo", dto.localidade);
    }

    @Test
    void buscarEnderecoPorCep_deveLancarIOExceptionEmErroHttp() throws Exception {
        String cep = "99999999";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<Object> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpResponseMock.body()).thenReturn("");
        when(httpClientMock.send(any(), any())).thenReturn(httpResponseMock);

        ViaCepService service = new ViaCepService();
        var httpClientField = ViaCepService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = ViaCepService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertThrows(IOException.class, () -> service.buscarEnderecoPorCep(cep));
    }

    @Test
    void buscarEnderecoPorCep_deveLancarIOExceptionEmJsonInvalido() throws Exception {
        String cep = "01001001";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<Object> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn("{invalid json}");
        when(httpClientMock.send(any(), any())).thenReturn(httpResponseMock);

        ViaCepService service = new ViaCepService();
        var httpClientField = ViaCepService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = ViaCepService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertThrows(IOException.class, () -> service.buscarEnderecoPorCep(cep));
    }

    @Test
    void buscarEnderecoPorCep_deveUsarCache() throws Exception {
        String cep = "01001000";
        String json = "{\"cep\":\"01001-000\",\"logradouro\":\"Praça da Sé\",\"bairro\":\"Sé\",\"localidade\":\"São Paulo\",\"uf\":\"SP\"}";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<Object> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(), any())).thenReturn(httpResponseMock);

        ViaCepService service = new ViaCepService();
        var httpClientField = ViaCepService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = ViaCepService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        // Primeira chamada: popula o cache
        ViaCepDTO dto1 = service.buscarEnderecoPorCep(cep);
        // Segunda chamada: deve usar o cache, não chamar o httpClient novamente
        ViaCepDTO dto2 = service.buscarEnderecoPorCep(cep);
        assertSame(dto1, dto2);
        verify(httpClientMock, times(1)).send(any(), any());
    }

    @Test
    void buscarEnderecoPorCep_deveLancarIOExceptionAposTentativas() throws Exception {
        String cep = "01001002";
        HttpClient httpClientMock = mock(HttpClient.class);
        when(httpClientMock.send(any(), any())).thenThrow(new IOException("Falha de rede"));

        ViaCepService service = new ViaCepService();
        var httpClientField = ViaCepService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = ViaCepService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertThrows(IOException.class, () -> service.buscarEnderecoPorCep(cep));
    }
}
