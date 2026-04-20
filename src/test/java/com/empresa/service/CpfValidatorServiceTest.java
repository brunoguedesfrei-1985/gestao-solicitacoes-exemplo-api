package com.empresa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CpfValidatorServiceTest {

    @Test
    void isCpfValido_deveRetornarTrueParaCpfValido() throws Exception {
        String cpf = "12345678901";
        String json = "true";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponseMock);

        CpfValidatorService service = new CpfValidatorService();
        var httpClientField = CpfValidatorService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = CpfValidatorService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertTrue(service.isCpfValido(cpf));
    }

    @Test
    void isCpfValido_deveRetornarFalseParaCpfNaoEncontrado() throws Exception {
        String cpf = "00000000000";
        String json = "{\"error\":\"cpf-nao-encotrado-receita-federal\"}";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(400);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponseMock);

        CpfValidatorService service = new CpfValidatorService();
        var httpClientField = CpfValidatorService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = CpfValidatorService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertFalse(service.isCpfValido(cpf));
    }

    @Test
    void isCpfValido_deveLancarIOExceptionEmErroHttp() throws Exception {
        String cpf = "99999999999";
        HttpClient httpClientMock = mock(HttpClient.class);
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(500);
        when(httpResponseMock.body()).thenReturn("");
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponseMock);

        CpfValidatorService service = new CpfValidatorService();
        var httpClientField = CpfValidatorService.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(service, httpClientMock);
        var objectMapperField = CpfValidatorService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(service, new ObjectMapper());

        assertThrows(IOException.class, () -> service.isCpfValido(cpf));
    }
}
