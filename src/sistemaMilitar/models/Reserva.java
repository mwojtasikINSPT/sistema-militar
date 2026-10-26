package sistemaMilitar.models;

public class Reserva {

    private String id;
    private String codigoCuartel;
    private String idSoldado;
    private boolean activo;

    public Reserva() {
    }

    public Reserva(String id, String codigoCuartel, String idSoldado, boolean activo) {
        this.id = id;
        this.codigoCuartel = codigoCuartel;
        this.idSoldado = idSoldado;
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

    public String getIdSoldado() {
        return idSoldado;
    }

    public void setIdSoldado(String idSoldado) {
        this.idSoldado = idSoldado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Reserva [ID: " + id + "] Cuartel: " + codigoCuartel + " | Soldado ID: " + idSoldado + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
