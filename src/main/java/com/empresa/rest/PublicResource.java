package com.empresa.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import com.empresa.auth.JwtUtil;
import com.empresa.domain.LoginRequest;
import com.empresa.domain.TokenResponse;
import com.empresa.domain.UsuarioRequest;
import com.empresa.dto.CargoDTO;
import com.empresa.dto.ResponseLoginDTO;
import com.empresa.dto.UsuarioDTO;
import com.empresa.service.CargoService;
import com.empresa.service.UsuarioService;
import com.empresa.validator.UsuarioValidator;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/public")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Public", description = "Operações públicas disponíveis sem autenticação")
public class PublicResource {

    private static final Logger LOG = Logger.getLogger(PublicResource.class);
    
    @Inject
    UsuarioValidator usuarioValidator;

    @Inject
    UsuarioService usuarioService;
    
    @Inject
    CargoService cargoService;
    
    /**
     * Endpoint público para listar cargos disponíveis
     */
    @Operation(summary = "Lista cargos disponíveis", description = "Retorna todos os cargos públicos cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de cargos", content = @Content(schema = @Schema(implementation = CargoDTO.class)))
    @GET
    @Path("/cargos")
    public List<CargoDTO> listarCargosPublico() {
        LOG.infof("{\"event\":\"listarCargosPublico\",\"status\":\"inicio\"}");
        List<CargoDTO> cargos = cargoService.listarTodos()
            .stream()
            .map(c -> new CargoDTO(c.id, c.nome))
            .collect(Collectors.toList());
        LOG.infof("{\"event\":\"listarCargosPublico\",\"status\":\"sucesso\",\"qtd\":%d}", cargos.size());
        return cargos;
    }

    @Operation(summary = "Cria um novo usuário", description = "Cria um usuário no sistema a partir dos dados enviados.")
    @APIResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @APIResponse(responseCode = "409", description = "Usuário já existe")
    @POST
    @Path("/usuarios")
    public Response criarUsuario(
        @RequestBody(description = "Dados do novo usuário", required = true, content = @Content(schema = @Schema(implementation = UsuarioRequest.class))) UsuarioRequest usuarioRequest) {
        LOG.infof("{\"event\":\"criarUsuario\",\"email\":\"%s\",\"status\":\"inicio\"}", usuarioRequest.email);
        try {
            usuarioValidator.validarParaCriar(usuarioRequest);
        } catch (IllegalArgumentException e) {
            LOG.errorf("{\"event\":\"criarUsuario\",\"email\":\"%s\",\"status\":\"erro\",\"mensagem\":\"%s\"}", usuarioRequest.email, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(java.util.Collections.singletonMap("erro", e.getMessage()))
                .build();
        }
        try {
            UsuarioDTO novoUsuario = usuarioService.criar(usuarioRequest);
            LOG.infof("{\"event\":\"criarUsuario\",\"email\":\"%s\",\"status\":\"sucesso\",\"usuarioId\":%s}", usuarioRequest.email, novoUsuario.id);
            return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
        } catch (IllegalArgumentException e) {
            LOG.errorf("{\"event\":\"criarUsuario\",\"email\":\"%s\",\"status\":\"erro\",\"mensagem\":\"%s\"}", usuarioRequest.email, e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @Operation(summary = "Autentica usuário e retorna token JWT", description = "Realiza login e retorna um token JWT válido para autenticação.")
    @APIResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @APIResponse(responseCode = "401", description = "Credenciais inválidas")
    @POST
    @Path("/login")
    public Response login(
        @RequestBody(description = "Credenciais de login", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class))) LoginRequest login) {
        LOG.infof("{\"event\":\"login\",\"email\":\"%s\",\"status\":\"inicio\"}", login.getEmail());
        UsuarioDTO usuario = usuarioService.autenticar(login.getEmail(), login.getSenha());
        if (usuario != null) {
            String token = JwtUtil.generateToken(usuario.email);
            LOG.infof("{\"event\":\"login\",\"email\":\"%s\",\"status\":\"sucesso\",\"usuarioId\":%s}", usuario.email, usuario.id);
            TokenResponse tokenResponse = new TokenResponse(token);
            return Response.ok()
                .header("Authorization", "Bearer " + token)
                .entity(new ResponseLoginDTO(usuario, tokenResponse))
                .build();
        } else {
            LOG.warnf("{\"event\":\"login\",\"email\":\"%s\",\"status\":\"falha\"}", login.getEmail());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário ou senha inválidos").build();
        }
    }
}
