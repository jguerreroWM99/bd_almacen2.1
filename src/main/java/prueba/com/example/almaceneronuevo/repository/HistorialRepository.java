package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.HistorialActividad;

import java.util.List;

public interface HistorialRepository extends JpaRepository<HistorialActividad, Long> {

    // Para el perfil de cualquier usuario (y vista principal del Coordinador)
    List<HistorialActividad> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    // Para el Administrador
    List<HistorialActividad> findAllByOrderByFechaHoraDesc();

    // Para el Supervisor (ve acciones de roles con nivel >= 2: Supervisor y Coordinador, excluyendo Admin nivel 1)
    List<HistorialActividad> findByUsuarioRolNivelJerarquiaGreaterThanEqualOrderByFechaHoraDesc(Integer nivelJerarquia);
}