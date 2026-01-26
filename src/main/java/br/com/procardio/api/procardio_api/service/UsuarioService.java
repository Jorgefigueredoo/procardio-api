package br.com.procardio.api.procardio_api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.procardio.api.procardio_api.dto.UsuarioDTO;
import br.com.procardio.api.procardio_api.exceptions.UsuarioNaoEncontradoException;
import br.com.procardio.api.procardio_api.model.Usuario;
import br.com.procardio.api.procardio_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario = usuario.toModel(usuarioDTO);

        usuario.setSenha(passwordEncoder.encode(usuarioDTO.senha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario salvarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = buscarUsuarioPorId(id);

        if (Objects.nonNull(usuario)) {
            usuario = usuario.toModel(usuarioDTO);
            usuario.setId(id);

            usuario.setSenha(passwordEncoder.encode(usuarioDTO.senha()));

            return usuarioRepository.save(usuario);
        }

        throw new UsuarioNaoEncontradoException(id);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // ✅ RENOMEADO: antes era listarTodosUsuarios()
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
