package sistemaMilitar.DTOs;

public class OficialDTO {

    private String id;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean activo;

    public OficialDTO() {
    }

    public OficialDTO(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = true;
    }

    public OficialDTO(String id, String dni, String nombre, String apellido, boolean activo) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Oficial [" + id + "] DNI: " + dni + " | " + apellido + ", " + nombre + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
