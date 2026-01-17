package br.com.procardio.api.procardio_api.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.procardio.api.procardio_api.model.Usuario;
import br.com.procardio.api.procardio_api.repository.UsuarioRepository;
import br.com.procardio.api.procardio_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);

        if (Objects.nonNull(tokenJWT)) {
            String subject = tokenService.getSubject(tokenJWT);

            Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);

            if (Objects.nonNull(usuario)) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filter.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest requisicao) {
        String cabecalhoAuthorization = requisicao.getHeader("Authorization");
        if (Objects.nonNull(cabecalhoAuthorization)) {
            return cabecalhoAuthorization.replace("Bearer ", "");
        }

        return null;

    }
}