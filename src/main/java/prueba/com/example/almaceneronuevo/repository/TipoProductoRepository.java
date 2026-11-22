package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.TipoProducto;

import java.util.List;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {
    List<TipoProducto> findByCategoriaId(Long categoriaId);
    boolean existsByNombreIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);
}