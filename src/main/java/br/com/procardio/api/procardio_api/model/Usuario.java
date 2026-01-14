package br.com.procardio.api.procardio_api.model;

import br.com.procardio.api.procardio_api.dto.UsuarioDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String senha;

    @Embedded
    private Endereço endereço;
    
     public Usuario toModel(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

        if (dto.cep() != null || dto.numero() != null || dto.complemento() != null) {
            Endereço endereco = new Endereço();

            endereco.setCep(dto.cep());
            endereco.setNumero(dto.numero());
            endereco.setComplemento(dto.complemento());
            
            usuario.setEndereço(endereco);
        }

        return usuario;
    }
}
