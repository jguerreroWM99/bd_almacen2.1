package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}