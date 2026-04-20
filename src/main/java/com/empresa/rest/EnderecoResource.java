package com.empresa.rest;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.empresa.dto.EnderecoDTO;
import com.empresa.model.Endereco;
import com.empresa.service.EnderecoService;
import com.empresa.validator.EnderecoValidator;

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
@Tag(name = "Endereço", description = "Operações relacionadas a endereços")
public class EnderecoResource {

	@Inject
	EnderecoService enderecoService;

	@Inject
	EnderecoValidator enderecoValidator;

	@GET
	@Path("/cep/{cep}")
	@Operation(summary = "Busca endereço por CEP", description = "Consulta endereço externo pelo CEP informado.")
	@APIResponse(responseCode = "200", description = "Endereço encontrado", content = @Content(schema = @Schema(implementation = EnderecoDTO.class)))
	@APIResponse(responseCode = "400", description = "CEP inválido ou erro na consulta")
	@APIResponse(responseCode = "404", description = "Endereço não encontrado")
	public Response buscarPorCep(
		@Parameter(description = "CEP para consulta", required = true, example = "01001000")
		@PathParam("cep") String cep) {
		try {
			EnderecoDTO dto = enderecoService.buscarPorCep(cep);
			if (dto == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			return Response.ok(dto).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao consultar CEP: " + e.getMessage())
					.build();
		}
	}

	@GET
	@Operation(summary = "Lista todos os endereços", description = "Retorna todos os endereços cadastrados.")
	@APIResponse(responseCode = "200", description = "Lista de endereços", content = @Content(schema = @Schema(implementation = Endereco.class)))
	public List<Endereco> listarTodos() {
		return enderecoService.listarTodos();
	}

	@GET
	@Path("/{id}")
	@Operation(summary = "Busca endereço por ID", description = "Retorna um endereço pelo seu identificador.")
	@APIResponse(responseCode = "200", description = "Endereço encontrado", content = @Content(schema = @Schema(implementation = Endereco.class)))
	@APIResponse(responseCode = "404", description = "Endereço não encontrado")
	public Response buscarPorId(
		@Parameter(description = "ID do endereço", required = true)
		@PathParam("id") Long id) {
		Endereco endereco = enderecoService.buscarPorId(id);
		if (endereco == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(endereco).build();
	}

	@POST
	@Operation(summary = "Cria um novo endereço", description = "Cria um endereço no sistema a partir dos dados enviados.")
	@APIResponse(responseCode = "201", description = "Endereço criado com sucesso", content = @Content(schema = @Schema(implementation = EnderecoDTO.class)))
	@APIResponse(responseCode = "400", description = "Dados inválidos")
	public Response criar(
		@RequestBody(description = "Dados do novo endereço", required = true, content = @Content(schema = @Schema(implementation = Endereco.class)))
		Endereco endereco) {
		try {
			enderecoValidator.validarParaCriar(endereco);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(java.util.Collections.singletonMap("erro", e.getMessage())).build();
		}
		try {
			EnderecoDTO novoEndereco = enderecoService.criar(endereco);
			return Response.status(Response.Status.CREATED).entity(novoEndereco).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	@PUT
	@Path("/{id}")
	@Operation(summary = "Atualiza um endereço", description = "Atualiza os dados de um endereço existente.")
	@APIResponse(responseCode = "200", description = "Endereço atualizado", content = @Content(schema = @Schema(implementation = Endereco.class)))
	@APIResponse(responseCode = "400", description = "Dados inválidos")
	@APIResponse(responseCode = "404", description = "Endereço não encontrado")
	public Response atualizar(
		@Parameter(description = "ID do endereço a ser atualizado", required = true)
		@PathParam("id") Long id,
		@RequestBody(description = "Dados do endereço para atualização", required = true, content = @Content(schema = @Schema(implementation = Endereco.class)))
		Endereco dados) {
		try {
			enderecoValidator.validarParaAtualizar(id, dados);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(java.util.Collections.singletonMap("erro", e.getMessage())).build();
		}
		Endereco endereco = enderecoService.atualizar(id, dados);
		if (endereco == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(endereco).build();
	}

	@DELETE
	@Path("/{id}")
	@Operation(summary = "Deleta um endereço", description = "Remove um endereço pelo seu identificador.")
	@APIResponse(responseCode = "204", description = "Endereço removido com sucesso")
	@APIResponse(responseCode = "400", description = "Não pode ser removido")
	@APIResponse(responseCode = "404", description = "Endereço não encontrado")
	public Response deletar(
		@Parameter(description = "ID do endereço a ser removido", required = true)
		@PathParam("id") Long id) {
		try {
			enderecoValidator.validarParaDeletar(id);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(java.util.Collections.singletonMap("erro", e.getMessage())).build();
		}
		boolean removido = enderecoService.deletar(id);
		if (removido) {
			return Response.noContent().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
