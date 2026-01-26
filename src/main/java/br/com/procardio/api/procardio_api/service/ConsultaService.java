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

    /**
     * Salvar ou atualizar consulta
     */
    public Consulta salvarConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    /**
     * Buscar consulta por ID
     */
    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }

    /**
     * Deletar consulta
     */
    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }

    /**
     * Buscar todas as consultas de um médico
     */
    public List<Consulta> buscarConsultaPorMedico(Medico medico) {
        return consultaRepository.findByMedico_Id(medico.getId());
    }

    /**
     * Buscar consulta específica por médico e data/hora
     */
    public Consulta buscarConsultaPorMedicoEDataHora(Long medicoId, LocalDateTime dataHora) {
        return consultaRepository.findByMedico_IdAndDataHora(medicoId, dataHora).orElse(null);
    }

    /**
     * Buscar consultas de um médico em um período específico
     */
    public List<Consulta> buscarConsultasPorPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return consultaRepository.findByMedico_IdAndDataHoraBetween(medicoId, dataInicio, dataFim);
    }

    /**
     * Buscar todas as consultas de um paciente
     */
    public List<Consulta> buscarConsultasPorPaciente(Long pacienteId) {
        return consultaRepository.findByPaciente_Id(pacienteId);
    }

    /**
     * Buscar consultas de um paciente em um período específico
     */
    public List<Consulta> buscarConsultasPorPacienteEPeriodo(Long pacienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return consultaRepository.findByPaciente_IdAndDataHoraBetween(pacienteId, dataInicio, dataFim);
    }

    /**
     * Listar todas as consultas
     */
    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }
}