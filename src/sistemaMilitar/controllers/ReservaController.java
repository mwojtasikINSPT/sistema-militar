package sistemaMilitar.controllers;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.ReservaDTO;
import sistemaMilitar.services.ReservaService;

public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public String registrarReserva(String codigoCuartel, String idSoldado) throws IOException {
        return reservaService.registrarReserva(codigoCuartel, idSoldado);
    }

    public List<ReservaDTO> listar() {
        return reservaService.listar();
    }

    public ReservaDTO buscarPorId(String id) {
        return reservaService.buscarPorId(id);
    }

    public void darDeBaja(String id) throws IOException {
        reservaService.darDeBaja(id);
    }

    public void reactivar(String id) throws IOException {
        reservaService.reactivar(id);
    }
}
