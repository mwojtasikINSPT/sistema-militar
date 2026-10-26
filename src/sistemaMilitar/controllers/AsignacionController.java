package sistemaMilitar.controllers;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.AsignacionDTO;
import sistemaMilitar.services.AsignacionService;

public class AsignacionController {

    private final AsignacionService asignacionService;

    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    public String registrarAsignacion(String codigoCuartel, String idOficial) throws IOException {
        return asignacionService.registrarAsignacion(codigoCuartel, idOficial);
    }

    public List<AsignacionDTO> listar() {
        return asignacionService.listar();
    }

    public AsignacionDTO buscarPorId(String id) {
        return asignacionService.buscarPorId(id);
    }

    public void darDeBaja(String id) throws IOException {
        asignacionService.darDeBaja(id);
    }

    public void reactivar(String id) throws IOException {
        asignacionService.reactivar(id);
    }
}
