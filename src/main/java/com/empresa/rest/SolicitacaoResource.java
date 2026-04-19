package com.empresa.rest;

import java.util.List;

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

    @Inject
    SolicitacaoService solicitacaoService;

    private static final Logger LOG = Logger.getLogger(SolicitacaoResource.class);


    @GET
    public Response listarTodas() {
        List<Solicitacao> lista = solicitacaoService.listarTodas();
        List<SolicitacaoDTO> dtos = lista.stream().map(SolicitacaoMapper::toDTO).toList();
        return Response.ok(dtos).build();
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
