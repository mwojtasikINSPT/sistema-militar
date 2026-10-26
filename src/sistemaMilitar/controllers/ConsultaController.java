package sistemaMilitar.controllers;

import java.util.List;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.services.ConsultaService;

public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    public String obtenerSoldadoConCuartel(String idODni) {
        return consultaService.obtenerSoldadoConCuartel(idODni);
    }

    public List<SoldadoDTO> obtenerSoldadosSupervisadosPorOficial(String idODniOficial) {
        return consultaService.obtenerSoldadosSupervisadosPorOficial(idODniOficial);
    }

    public OficialDTO obtenerOficialAsignadoASoldado(String idODniSoldado) {
        return consultaService.obtenerOficialAsignadoASoldado(idODniSoldado);
    }
}
