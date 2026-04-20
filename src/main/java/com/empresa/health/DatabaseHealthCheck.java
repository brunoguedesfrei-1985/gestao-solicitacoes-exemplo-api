package com.empresa.health;


import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    EntityManager em;

    @Override
    public HealthCheckResponse call() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return HealthCheckResponse.up("Banco de Dados");
        } catch (PersistenceException | IllegalStateException e) {
            return HealthCheckResponse.named("Banco de Dados")
                .down()
                .withData("erro", e.getMessage())
                .build();
        }
    }
}
