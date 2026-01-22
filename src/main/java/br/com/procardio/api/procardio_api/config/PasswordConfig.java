package br.com.procardio.api.procardio_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    

     @Bean
    // Configuração do codificador de senhas
    public PasswordEncoder passwordEncoder() {
        // Retorna uma instância do BCryptPasswordEncoder para codificação de senhas
        return new BCryptPasswordEncoder();
    }

}
