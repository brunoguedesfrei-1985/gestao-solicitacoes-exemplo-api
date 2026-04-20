package com.empresa.rest;



import java.util.List;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.logging.Logger;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.dto.SolicitacaoDTO;
import com.empresa.mapper.SolicitacaoMapper;
import com.empresa.model.Solicitacao;
import com.empresa.service.SolicitacaoService;
import com.empresa.validator.SolicitacaoValidator;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/solicitacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Solicitações", description = "Gerenciamento de solicitações")
public class SolicitacaoResource {
    @Inject
    SolicitacaoValidator solicitacaoValidator;

	private static final Logger LOG = Logger.getLogger(SolicitacaoResource.class);

	@Inject
    SolicitacaoService solicitacaoService;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/todas/demandante")
    @Counted(name = "solicitacoes_listar_todas_demandante_count", description = "Contador de chamadas ao endpoint de listagem de solicitações por demandante")
    @Operation(summary = "Listar solicitações do demandante logado", description = "Retorna todas as solicitações do usuário demandante logado, paginadas.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de solicitações retornada com sucesso",
            content = @Content(mediaType = "application/json")),
        @APIResponse(responseCode = "401", description = "Usuário não autenticado"),
        @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response listarTodas(
        @Parameter(description = "Página de resultados", example = "0") @QueryParam("page") @DefaultValue("0") int page,
        @Parameter(description = "Tamanho da página", example = "10") @QueryParam("size") @DefaultValue("10") int size
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
    @Counted(name = "solicitacoes_listar_todas_atendente_count", description = "Contador de chamadas ao endpoint de listagem de solicitações por atendente")
    @Operation(summary = "Listar solicitações do atendente logado", description = "Retorna todas as solicitações do usuário atendente logado, paginadas.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Lista de solicitações retornada com sucesso",
            content = @Content(mediaType = "application/json")),
        @APIResponse(responseCode = "401", description = "Usuário não autenticado"),
        @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response listarTodasUsuarioAtendente(
            @Parameter(description = "Página de resultados", example = "0") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @QueryParam("size") @DefaultValue("10") int size
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
    @Path("/demandante/{id}")
    @Operation(summary = "Buscar solicitação por id (demandante)", description = "Busca uma solicitação pelo id para o usuário demandante logado.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Solicitação encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.empresa.dto.SolicitacaoDTO.class))),
        @APIResponse(responseCode = "403", description = "Acesso negado"),
        @APIResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public Response buscarPorIdDemandante(
        @Parameter(description = "ID da solicitação", example = "1") @PathParam("id") Long id) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        Solicitacao s = solicitacaoService.buscarPorId(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (s.usuarioAtribuido == null || !email.equals(s.usuarioAtribuido.email)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Acesso negado à solicitação para demandante").build();
        }
        return Response.ok(SolicitacaoMapper.toDTO(s)).build();
    }

    @GET
    @Path("/atendente/{id}")
    @Operation(summary = "Buscar solicitação por id (atendente)", description = "Busca uma solicitação pelo id para o usuário atendente logado.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Solicitação encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.empresa.dto.SolicitacaoDTO.class))),
        @APIResponse(responseCode = "403", description = "Acesso negado"),
        @APIResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public Response buscarPorIdAtendente(
        @Parameter(description = "ID da solicitação", example = "1") @PathParam("id") Long id) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        Solicitacao s = solicitacaoService.buscarPorId(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (s.usuarioAtendente == null || !email.equals(s.usuarioAtendente.email)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Acesso negado à solicitação para atendente").build();
        }
        return Response.ok(SolicitacaoMapper.toDTO(s)).build();
    }

    @POST
    @Operation(summary = "Criar nova solicitação", description = "Cria uma nova solicitação.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Solicitação criada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.empresa.dto.SolicitacaoDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos"),
        @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response criar(
        @RequestBody(description = "Dados para criação da solicitação", required = true,
            content = @Content(schema = @Schema(implementation = com.empresa.domain.SolicitacaoRequest.class))) SolicitacaoRequest req) {
        try {
            solicitacaoValidator.validarParaCriar(req);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        try {
            SolicitacaoDTO nova = solicitacaoService.criar(req);
            return Response.status(Response.Status.CREATED).entity(nova).build();
        } catch (Exception e) {
            LOG.error("Erro ao criar solicitação", e);
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/demandante/{id}")
    @Operation(summary = "Atualizar solicitação (demandante)", description = "Atualiza uma solicitação como demandante.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Solicitação atualizada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.empresa.dto.SolicitacaoDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos"),
        @APIResponse(responseCode = "403", description = "Acesso negado"),
        @APIResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public Response atualizarDemandante(
        @Parameter(description = "ID da solicitação", example = "1") @PathParam("id") Long id,
        @RequestBody(description = "Dados para atualização da solicitação", required = true,
            content = @Content(schema = @Schema(implementation = com.empresa.model.Solicitacao.class))) Solicitacao dados) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        Solicitacao s = solicitacaoService.buscarPorId(id);
        if (s == null) {
            LOG.warnf("Solicitação id %d não encontrada para atualização", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (s.usuarioAtribuido == null || !email.equals(s.usuarioAtribuido.email)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Acesso negado à atualização para demandante").build();
        }
        try {
            solicitacaoValidator.validarParaAtualizar(id, dados);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        SolicitacaoDTO solicitacao = solicitacaoService.atualizar(id, dados);
        return Response.ok(solicitacao).build();
    }

    @PUT
    @Path("/atendente/{id}")
    @Operation(summary = "Atualizar solicitação (atendente)", description = "Atualiza uma solicitação como atendente.")
    @SecurityRequirement(name = "jwt")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Solicitação atualizada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.empresa.dto.SolicitacaoDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos"),
        @APIResponse(responseCode = "403", description = "Acesso negado"),
        @APIResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    public Response atualizarAtendente(
        @Parameter(description = "ID da solicitação", example = "1") @PathParam("id") Long id,
        @RequestBody(description = "Dados para atualização da solicitação", required = true,
            content = @Content(schema = @Schema(implementation = com.empresa.model.Solicitacao.class))) Solicitacao dados) {
        validateSecurityContext();
        String email = securityContext.getUserPrincipal().getName();
        Solicitacao s = solicitacaoService.buscarPorId(id);
        if (s == null) {
            LOG.warnf("Solicitação id %d não encontrada para atualização", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (s.usuarioAtendente == null || !email.equals(s.usuarioAtendente.email)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Acesso negado à atualização para atendente").build();
        }
        try {
            solicitacaoValidator.validarParaAtualizar(id, dados);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        SolicitacaoDTO solicitacao = solicitacaoService.atualizar(id, dados);
        return Response.ok(solicitacao).build();
    }


    // @DELETE
    // @Path("/{id}")
    // public Response deletar(@PathParam("id") Long id) {
    //     boolean deleted = solicitacaoService.deletar(id);
    //     if (deleted) {
    //         LOG.infof("Solicitação id %d deletada", id);
    //         return Response.noContent().build();
    //     } else {
    //         LOG.warnf("Solicitação id %d não encontrada para deleção", id);
    //         return Response.status(Response.Status.NOT_FOUND).build();
    //     }
    // }


}
