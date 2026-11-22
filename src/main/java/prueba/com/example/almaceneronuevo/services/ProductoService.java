package prueba.com.example.almaceneronuevo.services;

import prueba.com.example.almaceneronuevo.dto.ProductoRequestDTO;
import prueba.com.example.almaceneronuevo.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoResponseDTO> listarProductos();
    ProductoResponseDTO obtenerPorId(Long id);
    ProductoResponseDTO crearProducto(ProductoRequestDTO request, Long usuarioSolicitanteId);
    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request, Long usuarioSolicitanteId);
    void eliminarProducto(Long id, Long usuarioSolicitanteId);
}