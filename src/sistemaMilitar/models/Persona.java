package sistemaMilitar.models;

public abstract class Persona {

    private String id;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean activo;

    public Persona() {
        this.activo = true;
    }

    public Persona(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = true;
    }

    public Persona(String id, String dni, String nombre, String apellido, boolean activo) {
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

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | DNI: " + dni + " | Nombre: " + getNombreCompleto() + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
