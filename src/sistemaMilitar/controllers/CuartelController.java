package sistemaMilitar.controllers;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.CuartelDTO;
import sistemaMilitar.services.CuartelService;

public class CuartelController {

    private final CuartelService cuartelService;

    public CuartelController(CuartelService cuartelService) {
        this.cuartelService = cuartelService;
    }

    public void agregar(CuartelDTO dto) throws IOException {
        cuartelService.agregar(dto);
    }

    public List<CuartelDTO> listar() {
        return cuartelService.listar();
    }

    public CuartelDTO buscarPorId(String codigo) {
        return cuartelService.buscarPorId(codigo);
    }

    public void actualizar(CuartelDTO dto) throws IOException {
        cuartelService.actualizar(dto);
    }

    public void eliminar(String codigo) throws IOException {
        cuartelService.eliminar(codigo);
    }

    public void reactivar(String codigo) throws IOException {
        cuartelService.reactivar(codigo);
    }
}
