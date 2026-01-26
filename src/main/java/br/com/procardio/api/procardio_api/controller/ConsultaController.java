package br.com.procardio.api.procardio_api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.procardio_api.exceptions.ConflitoAgendamentoException;
import br.com.procardio.api.procardio_api.model.Consulta;
import br.com.procardio.api.procardio_api.model.Medico;
import br.com.procardio.api.procardio_api.model.Usuario;
import br.com.procardio.api.procardio_api.service.ConsultaService;
import br.com.procardio.api.procardio_api.service.MedicoService;
import br.com.procardio.api.procardio_api.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Criar uma nova consulta
     * Permite que pacientes e admins agendem consultas
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<?> criarConsulta(
            @Valid @RequestBody ConsultaDTO consultaDTO,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        // Buscar médico
        Medico medico = medicoService.buscarMedicoPorId(consultaDTO.medicoId());
        if (Objects.isNull(medico)) {
            return ResponseEntity.badRequest().body("Médico não encontrado");
        }

        // Buscar paciente
        Usuario paciente;
        if (consultaDTO.pacienteId() != null && usuarioAutenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Admin pode agendar para qualquer paciente
            paciente = usuarioService.buscarUsuarioPorId(consultaDTO.pacienteId());
            if (Objects.isNull(paciente)) {
                return ResponseEntity.badRequest().body("Paciente não encontrado");
            }
        } else {
            // Paciente agenda para si mesmo
            paciente = usuarioAutenticado;
        }

        // Verificar se já existe consulta nesse horário para o médico
        Consulta consultaExistente = consultaService.buscarConsultaPorMedicoEDataHora(
                medico.getId(),
                consultaDTO.dataHora());

        if (Objects.nonNull(consultaExistente)) {
            throw new ConflitoAgendamentoException(
                    "Já existe uma consulta agendada para este médico neste horário");
        }

        // Criar nova consulta
        Consulta novaConsulta = new Consulta();
        novaConsulta.setPaciente(paciente);
        novaConsulta.setMedico(medico);
        novaConsulta.setDataHora(consultaDTO.dataHora());

        Consulta consultaSalva = consultaService.salvarConsulta(novaConsulta);

        return ResponseEntity.status(HttpStatus.CREATED).body(consultaSalva);
    }

    /**
     * Buscar consulta por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<Consulta> buscarConsultaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Consulta consulta = consultaService.buscarConsultaPorId(id);

        if (Objects.isNull(consulta)) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se o usuário tem permissão para ver esta consulta
        boolean isAdmin = usuarioAutenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !consulta.getPaciente().getId().equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(consulta);
    }

    /**
     * Listar consultas do paciente autenticado
     */
    @GetMapping("/minhas-consultas")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<Consulta>> listarMinhasConsultas(
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        List<Consulta> consultas = consultaService.buscarConsultasPorPaciente(
                usuarioAutenticado.getId());

        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por médico
     */
    @GetMapping("/medico/{medicoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Consulta>> listarConsultasPorMedico(@PathVariable Long medicoId) {

        Medico medico = medicoService.buscarMedicoPorId(medicoId);

        if (Objects.isNull(medico)) {
            return ResponseEntity.notFound().build();
        }

        List<Consulta> consultas = consultaService.buscarConsultaPorMedico(medico);

        return ResponseEntity.ok(consultas);
    }

    /**
     * Listar consultas por paciente (apenas admin)
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {

        Usuario paciente = usuarioService.buscarUsuarioPorId(pacienteId);

        if (Objects.isNull(paciente)) {
            return ResponseEntity.notFound().build();
        }

        List<Consulta> consultas = consultaService.buscarConsultasPorPaciente(pacienteId);

        return ResponseEntity.ok(consultas);
    }

    /**
     * Buscar disponibilidade de horários de um médico
     */
    @GetMapping("/disponibilidade/{medicoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<List<Consulta>> verificarDisponibilidade(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        Medico medico = medicoService.buscarMedicoPorId(medicoId);

        if (Objects.isNull(medico)) {
            return ResponseEntity.notFound().build();
        }

        List<Consulta> consultasAgendadas = consultaService.buscarConsultasPorPeriodo(
                medicoId, dataInicio, dataFim);

        return ResponseEntity.ok(consultasAgendadas);
    }

    /**
     * Atualizar consulta
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<?> atualizarConsulta(
            @PathVariable Long id,
            @Valid @RequestBody ConsultaDTO consultaDTO,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Consulta consultaExistente = consultaService.buscarConsultaPorId(id);

        if (Objects.isNull(consultaExistente)) {
            return ResponseEntity.notFound().build();
        }

        // Verificar permissões
        boolean isAdmin = usuarioAutenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !consultaExistente.getPaciente().getId().equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verificar conflito de horário se a data/hora ou médico mudaram
        boolean mudouDataHora = !consultaExistente.getDataHora().equals(consultaDTO.dataHora());
        boolean mudouMedico = consultaExistente.getMedico().getId() != consultaDTO.medicoId();

        if (mudouDataHora || mudouMedico) {

            Consulta consultaConflitante = consultaService.buscarConsultaPorMedicoEDataHora(
                    consultaDTO.medicoId(),
                    consultaDTO.dataHora());

            if (Objects.nonNull(consultaConflitante) && consultaConflitante.getId() != id) {
                throw new ConflitoAgendamentoException(
                        "Já existe uma consulta agendada para este médico neste horário");
            }

            Medico novoMedico = medicoService.buscarMedicoPorId(consultaDTO.medicoId());
            if (Objects.isNull(novoMedico)) {
                return ResponseEntity.badRequest().body("Médico não encontrado");
            }

            consultaExistente.setMedico(novoMedico);
            consultaExistente.setDataHora(consultaDTO.dataHora());
        }

        Consulta consultaAtualizada = consultaService.salvarConsulta(consultaExistente);

        return ResponseEntity.ok(consultaAtualizada);
    }

    /**
     * Cancelar/deletar consulta
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
    public ResponseEntity<Void> cancelarConsulta(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Consulta consulta = consultaService.buscarConsultaPorId(id);

        if (Objects.isNull(consulta)) {
            return ResponseEntity.notFound().build();
        }

        // Verificar permissões
        boolean isAdmin = usuarioAutenticado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !consulta.getPaciente().getId().equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        consultaService.deletarConsulta(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * DTO para criação/atualização de consulta
     */
    public record ConsultaDTO(
            Long pacienteId,
            @jakarta.validation.constraints.NotNull(message = "O ID do médico é obrigatório")
            Long medicoId,
            @jakarta.validation.constraints.NotNull(message = "A data e hora da consulta são obrigatórias")
            LocalDateTime dataHora) {
    }
}