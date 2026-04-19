    
package com.empresa.rest;


import java.util.List;

import com.empresa.dto.EnderecoDTO;
import com.empresa.model.Endereco;
import com.empresa.service.EnderecoService;

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

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Inject
    EnderecoService enderecoService;
    
    @GET
    @Path("/cep/{cep}")
    public Response buscarPorCep(@PathParam("cep") String cep) {
        try {
            EnderecoDTO dto = enderecoService.buscarPorCep(cep);
            if (dto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao consultar CEP: " + e.getMessage()).build();
        }
    }


    @GET
    public List<Endereco> listarTodos() {
        return enderecoService.listarTodos();
    }


    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Endereco endereco = enderecoService.buscarPorId(id);
        if (endereco == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(endereco).build();
    }


    @POST
    public Response criar(Endereco endereco) {
        try {
            EnderecoDTO novoEndereco = enderecoService.criar(endereco);
            return Response.status(Response.Status.CREATED).entity(novoEndereco).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Endereco dados) {
        try {
            Endereco endereco = enderecoService.atualizar(id, dados);
            if (endereco == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(endereco).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean removido = enderecoService.deletar(id);
        if (removido) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
