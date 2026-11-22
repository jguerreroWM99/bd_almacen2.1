package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Modelo;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    List<Modelo> findByMarcaId(Long marcaId);
    boolean existsByNombreIgnoreCaseAndMarcaId(String nombre, Long marcaId);
}