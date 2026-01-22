package br.com.procardio.api.procardio_api.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.procardio.api.procardio_api.dto.TokenDTO;
import br.com.procardio.api.procardio_api.model.Usuario;
import br.com.procardio.api.procardio_api.service.GoogleAuthService;
import br.com.procardio.api.procardio_api.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private TokenService tokenService;

    // Configuração de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configurações de segurança
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.successHandler(googleLoginSuccessHandler()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    // Configuração do gerenciador de autenticação
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        // Retorna o gerenciador de autenticação a partir da configuração fornecida
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler googleLoginSuccessHandler() {
        return new AuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                Usuario usuario = googleAuthService.processarUsuarioGoogle(oAuth2User);

                String tokenJWT = tokenService.gerarToken(usuario);

                response.setContentType("application/json");

                TokenDTO tokenDTO = new TokenDTO(tokenJWT);

                ObjectMapper objectMapper = new ObjectMapper();

                String tokenJson = objectMapper.writeValueAsString(tokenDTO);

                response.getWriter().write(tokenJson);
                response.getWriter().flush();

            }

        };
    }

}