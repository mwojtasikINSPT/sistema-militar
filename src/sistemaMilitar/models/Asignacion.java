package sistemaMilitar.models;

public class Asignacion {

    private String id;
    private String codigoCuartel;
    private String idOficial;
    private boolean activo;

    public Asignacion() {
    }

    public Asignacion(String id, String codigoCuartel, String idOficial, boolean activo) {
        this.id = id;
        this.codigoCuartel = codigoCuartel;
        this.idOficial = idOficial;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoCuartel() {
        return codigoCuartel;
    }

    public void setCodigoCuartel(String codigoCuartel) {
        this.codigoCuartel = codigoCuartel;
    }

    public String getIdOficial() {
        return idOficial;
    }

    public void setIdOficial(String idOficial) {
        this.idOficial = idOficial;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Asignacion [ID: " + id + "] Cuartel: " + codigoCuartel + " | Oficial ID: " + idOficial + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
