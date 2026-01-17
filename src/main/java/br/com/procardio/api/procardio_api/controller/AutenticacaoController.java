package br.com.procardio.api.procardio_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.procardio_api.dto.LoginDTO;
import br.com.procardio.api.procardio_api.dto.TokenDTO;
import br.com.procardio.api.procardio_api.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> efetuarLogin(@Valid @RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha())
        );

        String tokenJWT = tokenService.gerarToken(authentication.getName());

        return ResponseEntity.ok(new TokenDTO(tokenJWT, "Bearer"));
    }
}
