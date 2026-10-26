package sistemaMilitar.DTOs;

public class AsignacionDTO {

    private String id;
    private String codigoCuartel;
    private String idOficial;
    private boolean activo;

    public AsignacionDTO() {
    }

    public AsignacionDTO(String codigoCuartel, String idOficial) {
        this.codigoCuartel = codigoCuartel;
        this.idOficial = idOficial;
        this.activo = true;
    }

    public AsignacionDTO(String id, String codigoCuartel, String idOficial, boolean activo) {
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
        return "Asignacion [" + id + "] Cuartel: " + codigoCuartel + " <-> Oficial: " + idOficial + " (" + (activo ? "Activa" : "Inactiva") + ")";
    }
}
