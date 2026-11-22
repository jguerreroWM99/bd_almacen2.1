package prueba.com.example.almaceneronuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.com.example.almaceneronuevo.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCodigoSkuIgnoreCase(String codigoSku);
    List<Producto> findByTipoProductoId(Long tipoProductoId);
    List<Producto> findByModeloId(Long modeloId);
}