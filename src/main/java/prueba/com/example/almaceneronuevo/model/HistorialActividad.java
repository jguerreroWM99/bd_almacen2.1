package prueba.com.example.almaceneronuevo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_actividades")
public class HistorialActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 40)
    private String modulo; // PRODUCTOS, CATALOGOS, USUARIOS, PERFIL

    @Column(nullable = false, length = 30)
    private String accion; // CREAR, EDITAR, ELIMINAR, CAMBIO_PASSWORD

    @Column(nullable = false, length = 500)
    private String detalle; // Ej: "Creó el producto Laptop Lenovo ThinkPad (SKU: LP-001)"

    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    public HistorialActividad() {
    }

    public HistorialActividad(Usuario usuario, String modulo, String accion, String detalle) {
        this.usuario = usuario;
        this.modulo = modulo;
        this.accion = accion;
        this.detalle = detalle;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}