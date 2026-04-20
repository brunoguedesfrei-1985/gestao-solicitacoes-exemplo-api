package com.empresa.resource;

import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseHealthResourceTest {
    @Test
    void checkDb_deveRetornarUpQuandoBancoOk() throws Exception {
        EntityManager em = mock(EntityManager.class);
        Query query = mock(Query.class);
        when(em.createNativeQuery("SELECT 1")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1);
        DatabaseHealthResource resource = new DatabaseHealthResource();
        // Injetar o mock via reflexão
        var field = DatabaseHealthResource.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(resource, em);
        Response resp = resource.checkDb();
        assertEquals(200, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("UP"));
    }

    @Test
    void checkDb_deveRetornarDownQuandoBancoFalha() throws Exception {
        EntityManager em = mock(EntityManager.class);
        when(em.createNativeQuery("SELECT 1")).thenThrow(new PersistenceException("Falha"));
        DatabaseHealthResource resource = new DatabaseHealthResource();
        var field = DatabaseHealthResource.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(resource, em);
        Response resp = resource.checkDb();
        assertEquals(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("DOWN"));
        assertTrue(resp.getEntity().toString().contains("Falha"));
    }
}
