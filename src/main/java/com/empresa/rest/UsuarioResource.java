package com.empresa.rest;


import java.util.List;

import org.jboss.logging.Logger;

import com.empresa.dto.UsuarioDTO;
import com.empresa.service.UsuarioService;

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
public class UsuarioResource {
	
	private static final Logger LOG = Logger.getLogger(UsuarioResource.class);

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioDTO> listarTodos() {
        LOG.info("Listando todos os usuários");
        return usuarioService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
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
    public Response deletar(@PathParam("id") Long id) {
        LOG.infof("Deletando usuário id %d", id);
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
