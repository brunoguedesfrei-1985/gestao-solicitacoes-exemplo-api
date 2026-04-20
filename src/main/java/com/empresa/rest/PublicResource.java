package com.empresa.rest;

import java.util.List;
import java.util.stream.Collectors;

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

    @POST
    @Path("/usuarios")
    public Response criarUsuario(UsuarioRequest usuarioRequest) {
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

    @POST
    @Path("/login")
    public Response login(LoginRequest login) {
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
