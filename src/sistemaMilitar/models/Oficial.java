package sistemaMilitar.models;

public class Oficial extends Persona {

    public Oficial() {
        super();
    }

    public Oficial(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    public Oficial(String id, String dni, String nombre, String apellido, boolean activo) {
        super(id, dni, nombre, apellido, activo);
    }

    @Override
    public String toString() {
        return "Oficial{" +
                "id='" + getId() + '\'' +
                ", dni='" + getDni() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", activo=" + (isActivo() ? "Sí" : "No") +
                '}';
    }
}
