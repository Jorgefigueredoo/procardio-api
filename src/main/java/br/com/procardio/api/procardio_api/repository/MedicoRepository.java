package br.com.procardio.api.procardio_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.procardio.api.procardio_api.enums.Especialidade;
import br.com.procardio.api.procardio_api.model.Medico;

public interface MedicoRepository extends JpaRepository <Medico, Long>{
    
    Optional <Medico> findByEmail(String email);

    Optional <Medico> findByCrm(String crm);

    List <Medico> findByNomeContainingIgnoreCase(String nome);

    List <Medico> findByEspecialidade(Especialidade especialidade);
}
