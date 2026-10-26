package sistemaMilitar.views;

import java.util.List;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.controllers.ConsultaController;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.OperacionNoPermitidaException;
import sistemaMilitar.utils.ConsolaUtils;

public class ConsultaView {

    private final ConsultaController consultaController;

    public ConsultaView(ConsultaController consultaController) {
        this.consultaController = consultaController;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.imprimirMenu(
                    "CONSULTAS OBLIGATORIAS DEL SISTEMA",
                    "1. Nombre del soldado con su cuartel asignado",
                    "2. Soldados supervisados por un oficial",
                    "3. Oficial asignado a un soldado",
                    "0. Volver al menú principal"
            );

            int opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    consultarSoldadoConCuartel();
                    break;
                case 2:
                    consultarSoldadosSupervisadosPorOficial();
                    break;
                case 3:
                    consultarOficialAsignadoASoldado();
                    break;
                case 0:
                    ConsolaUtils.imprimirMensaje("Volviendo al menú principal...");
                    salir = true;
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private void consultarSoldadoConCuartel() {
        ConsolaUtils.imprimirTitulo("CONSULTAR SOLDADO Y SU CUARTEL");
        try {
            String idODni = ConsolaUtils.leerTexto("Ingrese ID o DNI del Soldado");
            if (ConsolaUtils.esCancelacion(idODni)) return;

            String resultado = consultaController.obtenerSoldadoConCuartel(idODni);
            ConsolaUtils.imprimirMensaje(resultado);
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void consultarSoldadosSupervisadosPorOficial() {
        ConsolaUtils.imprimirTitulo("CONSULTAR SOLDADOS SUPERVISADOS POR UN OFICIAL");
        try {
            String idODni = ConsolaUtils.leerTexto("Ingrese ID o DNI del Oficial");
            if (ConsolaUtils.esCancelacion(idODni)) return;

            List<SoldadoDTO> soldados = consultaController.obtenerSoldadosSupervisadosPorOficial(idODni);
            if (soldados.isEmpty()) {
                ConsolaUtils.imprimirMensaje("El oficial no tiene soldados asignados en su cuartel.");
            } else {
                ConsolaUtils.imprimirMensaje("Soldados bajo la supervisión del oficial:");
                for (SoldadoDTO s : soldados) {
                    ConsolaUtils.imprimirMensaje("- " + s.getApellido() + ", " + s.getNombre() + " (ID: " + s.getId() + ", DNI: " + s.getDni() + ")");
                }
            }
            ConsolaUtils.imprimirCantidad(soldados.size());
        } catch (EntidadNoEncontradaException | OperacionNoPermitidaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void consultarOficialAsignadoASoldado() {
        ConsolaUtils.imprimirTitulo("CONSULTAR OFICIAL ASIGNADO A UN SOLDADO");
        try {
            String idODni = ConsolaUtils.leerTexto("Ingrese ID o DNI del Soldado");
            if (ConsolaUtils.esCancelacion(idODni)) return;

            OficialDTO oficial = consultaController.obtenerOficialAsignadoASoldado(idODni);
            ConsolaUtils.imprimirMensaje("Oficial a cargo del soldado:");
            ConsolaUtils.imprimirMensaje(oficial.toString());
        } catch (EntidadNoEncontradaException | OperacionNoPermitidaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }
}
