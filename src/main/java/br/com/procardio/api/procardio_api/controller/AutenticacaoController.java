package br.com.procardio.api.procardio_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.procardio_api.dto.LoginDTO;
import br.com.procardio.api.procardio_api.dto.TokenDTO;
import br.com.procardio.api.procardio_api.dto.UsuarioDTO;
import br.com.procardio.api.procardio_api.model.Usuario;
import br.com.procardio.api.procardio_api.service.TokenService;
import br.com.procardio.api.procardio_api.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private UsuarioService usuarioService;

    // Injetar o serviço de token
    @Autowired
    private TokenService tokenService;

    // Injetar o gerenciador de autenticação
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> efetuarLogin(@Valid @RequestBody LoginDTO loginDTO) {
        // Lógica de autenticação e geração de token
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        // Autenticar o usuário
        var authentication = authenticationManager.authenticate(authenticationToken);
        // Gerar o token JWT
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // Retornar o token na resposta
        return ResponseEntity.ok().body(new TokenDTO(tokenJWT));
    }

    // Endpoint para cadastrar um novo usuário
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Chama o serviço para salvar o novo usuário
        Usuario novoUsuario = usuarioService.salvarUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

}