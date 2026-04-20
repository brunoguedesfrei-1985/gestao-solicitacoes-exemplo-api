package com.empresa.exception;

import com.empresa.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionMapperTest {
    @Test
    void toResponse_deveRetornarBadRequestComMensagem() {
        ValidationExceptionMapper mapper = new ValidationExceptionMapper();
        IllegalArgumentException ex = new IllegalArgumentException("mensagem de erro");
        Response response = mapper.toResponse(ex);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertEquals("mensagem de erro", error.erro);
    }
}
