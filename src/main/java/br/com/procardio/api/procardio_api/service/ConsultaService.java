package br.com.procardio.api.procardio_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.procardio.api.procardio_api.model.Consulta;
import br.com.procardio.api.procardio_api.model.Medico;
import br.com.procardio.api.procardio_api.repository.ConsultaRepository;


@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;

    public Consulta salvarConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }

    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }

    public List<Consulta> buscarConsultaPorMedico(Medico medico) {
        return consultaRepository.findByMedico_Id(medico.getId());
    }

    public Consulta buscarConsultaPorMedicoEDataHora(Long medicoId, LocalDateTime dataHora) {
        return consultaRepository.findByMedico_IdAndDataHora(medicoId, dataHora).orElse(null);
    }

}