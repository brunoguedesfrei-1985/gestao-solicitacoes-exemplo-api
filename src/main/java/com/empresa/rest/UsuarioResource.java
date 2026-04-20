package com.empresa.rest;


import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.logging.Logger;

import com.empresa.dto.UsuarioDTO;
import com.empresa.service.UsuarioService;
import com.empresa.validator.UsuarioValidator;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuário", description = "Operações relacionadas a usuários do sistema")
public class UsuarioResource {
	
	private static final Logger LOG = Logger.getLogger(UsuarioResource.class);
	
	@Inject
	UsuarioValidator usuarioValidator;

    @Inject
    UsuarioService usuarioService;

    @GET
    @Operation(summary = "Lista todos os usuários", description = "Retorna todos os usuários cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de usuários", content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @SecurityRequirement(name = "jwt")
    public List<UsuarioDTO> listarTodos() {
        LOG.info("Listando todos os usuários");
        return usuarioService.listarTodos();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca usuário por ID", description = "Retorna um usuário pelo seu identificador.")
    @APIResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
        @SecurityRequirement(name = "jwt")
    public Response buscarPorId(
        @Parameter(description = "ID do usuário", required = true)
        @PathParam("id") Long id) {
        LOG.infof("Buscando usuário por id %d", id);
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            LOG.warnf("Usuário id %d não encontrado", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(usuario).build();
    }

    // Métodos públicos de criação de usuário e login foram movidos para PublicResource

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deleta um usuário", description = "Remove um usuário pelo seu identificador.")
    @APIResponse(responseCode = "204", description = "Usuário removido com sucesso")
    @APIResponse(responseCode = "400", description = "Não pode ser removido")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
        @SecurityRequirement(name = "jwt")
    public Response deletar(
        @Parameter(description = "ID do usuário a ser removido", required = true)
        @PathParam("id") Long id) {
        LOG.infof("Deletando usuário id %d", id);
        try {
            usuarioValidator.validarParaDeletar(id);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        boolean removido = usuarioService.deletar(id);
        if (removido) {
            LOG.infof("Usuário id %d deletado", id);
            return Response.noContent().build();
        } else {
            LOG.warnf("Usuário id %d não encontrado para deleção", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
