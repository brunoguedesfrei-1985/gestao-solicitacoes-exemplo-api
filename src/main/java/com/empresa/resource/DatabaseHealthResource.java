package com.empresa.resource;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health/db")
public class DatabaseHealthResource {

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDb() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return Response.ok("{\"status\":\"UP\"}").build();
        } catch (PersistenceException | IllegalStateException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"status\":\"DOWN\",\"erro\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
