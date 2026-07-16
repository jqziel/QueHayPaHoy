package qhph.vertareas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Clase que representa una tarea con sus propiedades y comportamiento
public class Tarea {
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaVencimiento;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private String prioridad;
    private String metodoEstudio;
    private boolean completada;
    private int ordenPersonalizado;

    // Crea una nueva tarea inicializando fechas automáticamente
    public Tarea(String nombre, String descripcion, LocalDateTime fechaVencimiento, String prioridad, String metodoEstudio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaCreacion = LocalDateTime.now();  // Fecha actual como creación
        this.fechaModificacion = LocalDateTime.now();  // Fecha actual como modificación
        this.prioridad = prioridad;
        this.metodoEstudio = metodoEstudio;
        this.completada = false;  // Por defecto no completada
        this.ordenPersonalizado = 0;  // Orden inicial
    }

    // Getters y setters que actualizan fechaModificacion cuando cambia el valor

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (!Objects.equals(this.nombre, nombre)) {
            this.nombre = nombre;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        if (!Objects.equals(this.descripcion, descripcion)) {
            this.descripcion = descripcion;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        if (!Objects.equals(this.fechaVencimiento, fechaVencimiento)) {
            this.fechaVencimiento = fechaVencimiento;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) {
        if (!Objects.equals(this.prioridad, prioridad)) {
            this.prioridad = prioridad;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public String getMetodoEstudio() { return metodoEstudio; }
    public void setMetodoEstudio(String metodoEstudio) {
        if (!Objects.equals(this.metodoEstudio, metodoEstudio)) {
            this.metodoEstudio = metodoEstudio;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) {
        if (this.completada != completada) {
            this.completada = completada;
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public int getOrdenPersonalizado() { return ordenPersonalizado; }
    public void setOrdenPersonalizado(int ordenPersonalizado) {
        this.ordenPersonalizado = ordenPersonalizado;
    }

    // Representación legible de la tarea para depuración
    @Override
    public String toString() {
        return "Tarea{" +
                "nombre='" + nombre + '\'' +
                ", fechaVencimiento=" + (fechaVencimiento != null ?
                fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A") +
                ", prioridad='" + prioridad + '\'' +
                ", completada=" + completada +
                '}';
    }
}