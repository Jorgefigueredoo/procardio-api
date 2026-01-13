package br.com.procardio.api.procardio_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.procardio.api.procardio_api.model.Consulta;

public interface ConsultaRepository extends JpaRepository <Consulta, Long> {
    
    List <Consulta> findByMedico_Id(Long medicoId);

    List <Consulta> findByMedico_IdAndDataHoraBetween (Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);

    List <Consulta> findByPaciente_Id (Long pacienteId);

    List <Consulta> findByPaciente_IdAndDataHoraBetwwen(Long pacienteId, LocalDateTime dataInicio, LocalDateTime dataFim);
}
