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

    // Método que intercepta a requisição para validar o token JWT
    @Override
    // metodo que filtra a requisição uma única vez
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        // Recupera o token JWT do cabeçalho da requisição
        String tokenJWT = recuperarToken(request);
        
        // Verifica se o token JWT é válido
        if (Objects.nonNull(tokenJWT)) {
            // Recupera o assunto (subject) do token JWT
            String subject = tokenService.getSubject(tokenJWT);

            // Busca o usuário no banco de dados com base no assunto recuperado
            Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);

            // Se o usuário for encontrado, cria um objeto de autenticação e o define no contexto de segurança
            if (Objects.nonNull(usuario)) {
                // Cria o objeto de autenticação com o usuário e suas autoridades
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // Define a autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua a cadeia de filtros para qualquer parte do projeto
        filter.doFilter(request, response);
    }

    // Método para recuperar o token JWT do cabeçalho da requisição
    private String recuperarToken(HttpServletRequest requisicao) {
        // Recupera o cabeçalho "Authorization" da requisição
        String cabecalhoAuthorization = requisicao.getHeader("Authorization");
        // Verifica se o cabeçalho não é nulo e remove o prefixo "Bearer "
        if (Objects.nonNull(cabecalhoAuthorization)) {
            // Retorna o token JWT sem o prefixo "Bearer "
            return cabecalhoAuthorization.replace("Bearer ", "");
        }

        // Retorna nulo se o cabeçalho não estiver presente
        return null;

    }
}