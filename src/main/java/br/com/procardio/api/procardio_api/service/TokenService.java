package br.com.procardio.api.procardio_api.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String secret;

    // ✅ Novo: gera token usando email (String) - funciona com
    // authentication.getName()
    public String gerarToken(String email) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withSubject(email)
                    .withIssuer("API Procardio")
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);

        } catch (JWTCreationException ex) {
            throw new RuntimeException("Erro ao gerar token JWT", ex);
        }
    }

    public String getSubject(String jwt) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("API Procardio")
                    .build()
                    .verify(jwt)
                    .getSubject();

        } catch (Exception ex) {
            throw new RuntimeException("Token JWT inválido ou expirado", ex);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
