package com.empresa.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import com.empresa.model.Cargo;
import com.empresa.service.CargoService;

import jakarta.inject.Inject;
import com.empresa.validator.CargoValidator;
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
@Tag(name = "Cargo", description = "Operações relacionadas a cargos/funções de usuários")
public class CargoResource {
    
    @Inject
    CargoService cargoService;
    
    @Inject
    CargoValidator cargoValidator;

    @GET
    @Operation(summary = "Lista todos os cargos", description = "Retorna todos os cargos cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de cargos", content = @Content(schema = @Schema(implementation = Cargo.class, description = "Lista de cargos")))
    public List<Cargo> listarTodos() {
        return cargoService.listarTodos();
    }


    @GET
    @Path("/{id}")
    @Operation(summary = "Busca cargo por ID", description = "Retorna um cargo pelo seu identificador.")
    @APIResponse(responseCode = "200", description = "Cargo encontrado", content = @Content(schema = @Schema(implementation = Cargo.class)))
    @APIResponse(responseCode = "404", description = "Cargo não encontrado")
    public Response buscarPorId(
        @Parameter(description = "ID do cargo", required = true)
        @PathParam("id") Long id) {
        Cargo cargo = cargoService.buscarPorId(id);
        if (cargo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cargo).build();
    }


    @POST
    @Operation(summary = "Cria um novo cargo", description = "Cria um cargo no sistema a partir dos dados enviados.")
    @APIResponse(responseCode = "201", description = "Cargo criado com sucesso", content = @Content(schema = @Schema(implementation = Cargo.class)))
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Response criar(
        @RequestBody(description = "Dados do novo cargo", required = true, content = @Content(schema = @Schema(implementation = Cargo.class)))
        Cargo cargo) {
        try {
            cargoValidator.validarParaCriar(cargo);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        Cargo novoCargo = cargoService.criar(cargo);
        return Response.status(Response.Status.CREATED).entity(novoCargo).build();
    }


    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza um cargo", description = "Atualiza os dados de um cargo existente.")
    @APIResponse(responseCode = "200", description = "Cargo atualizado", content = @Content(schema = @Schema(implementation = Cargo.class)))
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "404", description = "Cargo não encontrado")
    public Response atualizar(
        @Parameter(description = "ID do cargo a ser atualizado", required = true)
        @PathParam("id") Long id,
        @RequestBody(description = "Dados do cargo para atualização", required = true, content = @Content(schema = @Schema(implementation = Cargo.class)))
        Cargo dados) {
        try {
            cargoValidator.validarParaAtualizar(id, dados);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        Cargo cargo = cargoService.atualizar(id, dados);
        if (cargo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cargo).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deleta um cargo", description = "Remove um cargo pelo seu identificador.")
    @APIResponse(responseCode = "204", description = "Cargo removido com sucesso")
    @APIResponse(responseCode = "400", description = "Não pode ser removido")
    @APIResponse(responseCode = "404", description = "Cargo não encontrado")
    public Response deletar(
        @Parameter(description = "ID do cargo a ser removido", required = true)
        @PathParam("id") Long id) {
        try {
            cargoValidator.validarParaDeletar(id);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        boolean excluido = cargoService.deletar(id);
        if (excluido) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
