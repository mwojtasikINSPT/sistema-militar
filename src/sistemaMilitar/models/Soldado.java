package sistemaMilitar.models;

public class Soldado extends Persona {

    public Soldado() {
        super();
    }

    public Soldado(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    public Soldado(String id, String dni, String nombre, String apellido, boolean activo) {
        super(id, dni, nombre, apellido, activo);
    }

    @Override
    public String toString() {
        return "Soldado{" +
                "id='" + getId() + '\'' +
                ", dni='" + getDni() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", activo=" + (isActivo() ? "Sí" : "No") +
                '}';
    }
}
