package sistemaMilitar.views;

import java.util.List;
import sistemaMilitar.DTOs.AsignacionDTO;
import sistemaMilitar.controllers.AsignacionController;
import sistemaMilitar.utils.ConsolaUtils;

public class AsignacionView {

    private final AsignacionController asignacionController;

    public AsignacionView(AsignacionController asignacionController) {
        this.asignacionController = asignacionController;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE ASIGNACIONES (OFICIAL - CUARTEL 1:1)",
                    "1. Registrar nueva asignación",
                    "2. Listar todas las asignaciones",
                    "3. Buscar asignación por ID",
                    "4. Dar de baja asignación",
                    "5. Reactivar asignación",
                    "0. Volver al menú principal"
            );

            int opcion = ConsolaUtils.leerEntero("Seleccioná una opción");

            switch (opcion) {
                case 1:
                    registrarAsignacion();
                    break;
                case 2:
                    listarAsignaciones();
                    break;
                case 3:
                    buscarAsignacionPorId();
                    break;
                case 4:
                    darDeBajaAsignacion();
                    break;
                case 5:
                    reactivarAsignacion();
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción no válida.");
            }
        }
    }

    private void registrarAsignacion() {
        ConsolaUtils.imprimirTitulo("REGISTRAR ASIGNACIÓN DE OFICIAL A CUARTEL");
        String codCuartel = ConsolaUtils.leerTexto("Código del Cuartel (ej: CUA0001)");
        if (ConsolaUtils.esCancelacion(codCuartel)) return;

        String idOficial = ConsolaUtils.leerTexto("ID del Oficial (ej: OFI0001)");
        if (ConsolaUtils.esCancelacion(idOficial)) return;

        try {
            String idAsignacion = asignacionController.registrarAsignacion(codCuartel, idOficial);
            ConsolaUtils.imprimirRegistroCreado("Asignación", idAsignacion);
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void listarAsignaciones() {
        ConsolaUtils.imprimirTitulo("LISTADO DE ASIGNACIONES");
        try {
            List<AsignacionDTO> asignaciones = asignacionController.listar();
            if (asignaciones.isEmpty()) {
                ConsolaUtils.imprimirMensaje("No hay asignaciones registradas.");
            } else {
                for (AsignacionDTO a : asignaciones) {
                    ConsolaUtils.imprimirMensaje(a.toString());
                }
                ConsolaUtils.imprimirCantidad(asignaciones.size());
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void buscarAsignacionPorId() {
        ConsolaUtils.imprimirTitulo("BUSCAR ASIGNACIÓN POR ID");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Asignación (ej: ASG0001)");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            AsignacionDTO dto = asignacionController.buscarPorId(id);
            ConsolaUtils.imprimirMensaje(dto.toString());
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void darDeBajaAsignacion() {
        ConsolaUtils.imprimirTitulo("DAR DE BAJA ASIGNACIÓN");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Asignación a dar de baja");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            if (ConsolaUtils.confirmar("¿Está seguro de dar de baja la asignación " + id + "?")) {
                asignacionController.darDeBaja(id);
                ConsolaUtils.imprimirMensaje("Asignación dada de baja correctamente.");
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void reactivarAsignacion() {
        ConsolaUtils.imprimirTitulo("REACTIVAR ASIGNACIÓN");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Asignación a reactivar");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            if (ConsolaUtils.confirmar("¿Está seguro de reactivar la asignación " + id + "?")) {
                asignacionController.reactivar(id);
                ConsolaUtils.imprimirMensaje("Asignación reactivada correctamente.");
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }
}
