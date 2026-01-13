package br.com.procardio.api.procardio_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.procardio.api.procardio_api.enums.Especialidade;
import br.com.procardio.api.procardio_api.model.Medico;
import br.com.procardio.api.procardio_api.repository.MedicoRepository;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public Medico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id).orElse(null);
    }

    public Medico buscarMedicoPorEmail(String email) {
        return medicoRepository.findByEmail(email).orElse(null);
    }

    public Medico salvarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public void deletarMedico(Long id) {
        medicoRepository.deleteById(id);
    }

    public Medico buscarMedicoPorCrm(String crm) {
        return medicoRepository.findByCrm(crm).orElse(null);
    }

    public List<Medico> buscarPorEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidade(especialidade);
    }

    public List<Medico> listarTodosMedicos() {
        return medicoRepository.findAll();
    }
}
