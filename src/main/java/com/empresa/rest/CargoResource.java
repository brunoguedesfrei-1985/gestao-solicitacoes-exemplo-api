package com.empresa.rest;


import java.util.List;

import com.empresa.model.Cargo;
import com.empresa.service.CargoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cargos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CargoResource {

    @Inject
    CargoService cargoService;


    @GET
    public List<Cargo> listarTodos() {
        return cargoService.listarTodos();
    }


    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Cargo cargo = cargoService.buscarPorId(id);
        if (cargo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cargo).build();
    }


    @POST
    public Response criar(Cargo cargo) {
        Cargo novoCargo = cargoService.criar(cargo);
        return Response.status(Response.Status.CREATED).entity(novoCargo).build();
    }


    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Cargo dados) {
        Cargo cargo = cargoService.atualizar(id, dados);
        if (cargo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cargo).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean excluido = cargoService.deletar(id);
        if (excluido) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
