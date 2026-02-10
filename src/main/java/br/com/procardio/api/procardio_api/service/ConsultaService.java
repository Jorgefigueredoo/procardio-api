package br.com.procardio.api.procardio_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.procardio.api.procardio_api.dto.ConsultaAgendadaEvent;
import br.com.procardio.api.procardio_api.model.Consulta;
import br.com.procardio.api.procardio_api.model.Medico;
import br.com.procardio.api.procardio_api.repository.ConsultaRepository;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.eventos}")
    private String exchangeEventos;

    /**
     * Salvar ou atualizar consulta
     * (com verificação de conflito: médico já tem consulta na mesma data/hora)
     */
    public Consulta salvarConsulta(Consulta consulta) {

        // Verifica se já existe consulta para o mesmo médico e mesmo horário
        java.util.Optional<Consulta> consultaExistente = consultaRepository.findByMedico_IdAndDataHora(
                consulta.getMedico().getId(),
                consulta.getDataHora());

        // Se existir e não for a mesma consulta (caso de update), bloqueia
        if (consultaExistente.isPresent()) {
            Long idExistente = consultaExistente.get().getId();
            Long idAtual = consulta.getId();

            boolean ehOutraConsulta = (idAtual == null) || !idExistente.equals(idAtual);

            if (ehOutraConsulta) {
                throw new br.com.procardio.api.procardio_api.exceptions.ConflitoAgendamentoException(
                        "Conflito de agendamento: o médico já possui uma consulta marcada nesta data para esse horário.");
            }
        }

        // Salva a consulta
        Consulta consultaSalva = consultaRepository.save(consulta);

        // Monta o evento usando a consulta salva
        ConsultaAgendadaEvent evento = new ConsultaAgendadaEvent(
                consultaSalva.getId(),
                consultaSalva.getPaciente().getId(),
                consultaSalva.getPaciente().getNome(),
                consultaSalva.getPaciente().getEmail(),
                consultaSalva.getMedico().getNome(),
                consultaSalva.getMedico().getEspecialidade().name(), // ou .toString()
                consultaSalva.getDataHora());

        // Publica no RabbitMQ
        rabbitTemplate.convertAndSend(exchangeEventos, "consulta.agendada", evento);

        return consultaSalva;
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
    public List<Consulta> buscarConsultasPorPacienteEPeriodo(Long pacienteId, LocalDateTime dataInicio,
            LocalDateTime dataFim) {
        return consultaRepository.findByPaciente_IdAndDataHoraBetween(pacienteId, dataInicio, dataFim);
    }

    /**
     * Listar todas as consultas
     */
    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }
}
