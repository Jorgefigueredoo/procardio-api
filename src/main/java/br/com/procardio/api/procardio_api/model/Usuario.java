package br.com.procardio.api.procardio_api.model;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.procardio.api.procardio_api.dto.UsuarioDTO;
import br.com.procardio.api.procardio_api.enums.Perfil;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_usuarios")

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Embedded // Incorporação do endereço como um componente embutido
    private Endereço endereco;

    @ElementCollection(fetch = FetchType.EAGER)
    // Associação dos perfis de usuário
    @CollectionTable(name = "tb_perfis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "perfis")
    // Set de perfis associados ao usuário (SET não duplica perfis)
    private Set<Perfil> perfis;

    // Método para adicionar um perfil ao usuário
    public void adicionarPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    // Método para converter UsuarioDTO em Usuario
    public Usuario toModel(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        // Mapeamento dos campos do DTO para a entidade
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

        if (Objects.nonNull(dto.perfis())) {
            dto.perfis().stream().forEach(perfil -> {
                if (Objects.nonNull(perfil)) {
                    this.adicionarPerfil(perfil);
                }
            });
        }

        // Mapeamento do endereço se fornecido
        if (dto.cep() != null || dto.numero() != null || dto.complemento() != null) {
            Endereço endereco = new Endereço();

            endereco.setCep(dto.cep());
            endereco.setNumero(dto.numero());
            endereco.setComplemento(dto.complemento());

            usuario.setEndereco(endereco);
        }

        // Retorna a entidade Usuario preenchida
        return usuario;
    }

    // Implementação dos métodos da interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getRole()))
                .collect(Collectors.toList());
    }

    // Implementação do método getPassword
    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    // Implementação do método getUsername
    @Override
    public String getUsername() {
        return this.email;
    }

    // Implementação do método isAccountNonExpired (conta não expirada)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Implementação do método isAccountNonLocked (conta não bloqueada)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Implementação do método isCredentialsNonExpired (credenciais não expiradas)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Implementação do método isEnabled (conta habilitada)
    @Override
    public boolean isEnabled() {
        return true;
    }

}