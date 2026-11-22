package prueba.com.example.almaceneronuevo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre; // Ejemplo: ADMIN, SUPERVISOR, COORDINADOR

    @Column(name = "nivel_jerarquia", nullable = false)
    private Integer nivelJerarquia; // 1 = Admin, 2 = Supervisor, 3 = Coordinador

    @Column(length = 150)
    private String descripcion;

    public Rol() {
    }

    public Rol(String nombre, Integer nivelJerarquia, String descripcion) {
        this.nombre = nombre;
        this.nivelJerarquia = nivelJerarquia;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNivelJerarquia() {
        return nivelJerarquia;
    }

    public void setNivelJerarquia(Integer nivelJerarquia) {
        this.nivelJerarquia = nivelJerarquia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}