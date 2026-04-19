package com.empresa.rest;


import java.util.List;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import org.jboss.logging.Logger;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.dto.SolicitacaoDTO;
import com.empresa.mapper.SolicitacaoMapper;
import com.empresa.model.Solicitacao;
import com.empresa.service.SolicitacaoService;

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

@Path("/solicitacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SolicitacaoResource {

	private static final Logger LOG = Logger.getLogger(SolicitacaoResource.class);

	@Inject
    SolicitacaoService solicitacaoService;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/todas/demandante")
    public Response listarTodas(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("10") int size
    ) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        long total = solicitacaoService.countPorUsuarioDemandante(email);
        List<Solicitacao> lista = solicitacaoService.listarTodasPorUsuario(email, page, size);
        List<SolicitacaoDTO> dtos = lista.stream().map(SolicitacaoMapper::toDTO).toList();
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("total", total);
        response.put("items", dtos);
        return Response.ok(response).build();
    }
    
    @GET
    @Path("/todas/atendente")
    public Response listarTodasUsuarioAtendente(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
        ) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        long total = solicitacaoService.countPorUsuarioAtendente(email);
        List<Solicitacao> lista = solicitacaoService.listarTodasPorUsuarioAtendente(email, page, size);
        List<SolicitacaoDTO> dtos = lista.stream().map(SolicitacaoMapper::toDTO).toList();
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("total", total);
        response.put("items", dtos);
        return Response.ok(response).build();
    }

	private Response validateSecurityContext() {
		if (securityContext.getUserPrincipal() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário não autenticado").build();
        }
		return null;
	}


    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Solicitacao s = solicitacaoService.buscarPorId(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(SolicitacaoMapper.toDTO(s)).build();
    }

    @POST
    public Response criar(SolicitacaoRequest req) {
        try {
            SolicitacaoDTO nova = solicitacaoService.criar(req);
            return Response.status(Response.Status.CREATED).entity(nova).build();
        } catch (Exception e) {
            LOG.error("Erro ao criar solicitação", e);
            return Response.serverError().build();
        }
    }


    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Solicitacao dados) {
        SolicitacaoDTO solicitacao = solicitacaoService.atualizar(id, dados);
        if (solicitacao == null) {
            LOG.warnf("Solicitação id %d não encontrada para atualização", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(solicitacao).build();
    }


    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = solicitacaoService.deletar(id);
        if (deleted) {
            LOG.infof("Solicitação id %d deletada", id);
            return Response.noContent().build();
        } else {
            LOG.warnf("Solicitação id %d não encontrada para deleção", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
