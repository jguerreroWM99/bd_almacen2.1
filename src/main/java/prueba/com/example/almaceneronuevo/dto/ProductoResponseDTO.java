package prueba.com.example.almaceneronuevo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoResponseDTO {

    private Long id;
    private String codigoSku;
    private String nombre;
    private String especificaciones;
    private BigDecimal precio;
    private Integer stock;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    private Long tipoProductoId;
    private String tipoProductoNombre;
    private Long categoriaId;
    private String categoriaNombre;

    private Long modeloId;
    private String modeloNombre;
    private Long marcaId;
    private String marcaNombre;

    public ProductoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoSku() {
        return codigoSku;
    }

    public void setCodigoSku(String codigoSku) {
        this.codigoSku = codigoSku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(Long tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public String getTipoProductoNombre() {
        return tipoProductoNombre;
    }

    public void setTipoProductoNombre(String tipoProductoNombre) {
        this.tipoProductoNombre = tipoProductoNombre;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public String getModeloNombre() {
        return modeloNombre;
    }

    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
    }
}