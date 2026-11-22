package prueba.com.example.almaceneronuevo.services;

import prueba.com.example.almaceneronuevo.dto.CategoriaDTO;
import prueba.com.example.almaceneronuevo.dto.MarcaDTO;
import prueba.com.example.almaceneronuevo.dto.ModeloDTO;
import prueba.com.example.almaceneronuevo.dto.TipoProductoDTO;

import java.util.List;

public interface CatalogoService {

    // Categorías
    List<CategoriaDTO> listarCategorias();
    CategoriaDTO crearCategoria(CategoriaDTO dto, Long usuarioSolicitanteId);
    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto, Long usuarioSolicitanteId);
    void eliminarCategoria(Long id, Long usuarioSolicitanteId);

    // Tipos de Producto
    List<TipoProductoDTO> listarTiposProducto();
    List<TipoProductoDTO> listarTiposPorCategoria(Long categoriaId);
    TipoProductoDTO crearTipoProducto(TipoProductoDTO dto, Long usuarioSolicitanteId);
    TipoProductoDTO actualizarTipoProducto(Long id, TipoProductoDTO dto, Long usuarioSolicitanteId);
    void eliminarTipoProducto(Long id, Long usuarioSolicitanteId);

    // Marcas
    List<MarcaDTO> listarMarcas();
    MarcaDTO crearMarca(MarcaDTO dto, Long usuarioSolicitanteId);
    MarcaDTO actualizarMarca(Long id, MarcaDTO dto, Long usuarioSolicitanteId);
    void eliminarMarca(Long id, Long usuarioSolicitanteId);

    // Modelos
    List<ModeloDTO> listarModelos();
    List<ModeloDTO> listarModelosPorMarca(Long marcaId);
    ModeloDTO crearModelo(ModeloDTO dto, Long usuarioSolicitanteId);
    ModeloDTO actualizarModelo(Long id, ModeloDTO dto, Long usuarioSolicitanteId);
    void eliminarModelo(Long id, Long usuarioSolicitanteId);
}