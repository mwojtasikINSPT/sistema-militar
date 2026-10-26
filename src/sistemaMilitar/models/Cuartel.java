package sistemaMilitar.models;

public class Cuartel {

    private String codigo;
    private int capacidad;
    private boolean activo;

    public Cuartel() {
        this.activo = true;
    }

    public Cuartel(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.activo = true;
    }

    public Cuartel(String codigo, int capacidad, boolean activo) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Cuartel{" +
                "codigo='" + codigo + '\'' +
                ", capacidad=" + capacidad +
                ", activo=" + (activo ? "Sí" : "No") +
                '}';
    }
}
