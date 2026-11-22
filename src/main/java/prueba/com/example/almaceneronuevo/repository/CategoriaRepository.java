package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}