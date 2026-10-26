package sistemaMilitar.views;

import java.util.List;
import sistemaMilitar.DTOs.ReservaDTO;
import sistemaMilitar.controllers.ReservaController;
import sistemaMilitar.utils.ConsolaUtils;

public class ReservaView {

    private final ReservaController reservaController;

    public ReservaView(ReservaController reservaController) {
        this.reservaController = reservaController;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE RESERVAS (CUARTEL - SOLDADO)",
                    "1. Registrar nueva reserva",
                    "2. Listar todas las reservas",
                    "3. Buscar reserva por ID",
                    "4. Dar de baja reserva",
                    "5. Reactivar reserva",
                    "0. Volver al menú principal"
            );

            int opcion = ConsolaUtils.leerEntero("Seleccioná una opción");

            switch (opcion) {
                case 1:
                    registrarReserva();
                    break;
                case 2:
                    listarReservas();
                    break;
                case 3:
                    buscarReservaPorId();
                    break;
                case 4:
                    darDeBajaReserva();
                    break;
                case 5:
                    reactivarReserva();
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción no válida.");
            }
        }
    }

    private void registrarReserva() {
        ConsolaUtils.imprimirTitulo("REGISTRAR RESERVA DE SOLDADO EN CUARTEL");
        String codCuartel = ConsolaUtils.leerTexto("Código del Cuartel (ej: CUA0001)");
        if (ConsolaUtils.esCancelacion(codCuartel)) return;

        String idSoldado = ConsolaUtils.leerTexto("ID del Soldado (ej: SOL0001)");
        if (ConsolaUtils.esCancelacion(idSoldado)) return;

        try {
            String idReserva = reservaController.registrarReserva(codCuartel, idSoldado);
            ConsolaUtils.imprimirRegistroCreado("Reserva", idReserva);
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void listarReservas() {
        ConsolaUtils.imprimirTitulo("LISTADO DE RESERVAS");
        try {
            List<ReservaDTO> reservas = reservaController.listar();
            if (reservas.isEmpty()) {
                ConsolaUtils.imprimirMensaje("No hay reservas registradas.");
            } else {
                for (ReservaDTO r : reservas) {
                    ConsolaUtils.imprimirMensaje(r.toString());
                }
                ConsolaUtils.imprimirCantidad(reservas.size());
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void buscarReservaPorId() {
        ConsolaUtils.imprimirTitulo("BUSCAR RESERVA POR ID");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Reserva (ej: RES0001)");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            ReservaDTO dto = reservaController.buscarPorId(id);
            ConsolaUtils.imprimirMensaje(dto.toString());
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void darDeBajaReserva() {
        ConsolaUtils.imprimirTitulo("DAR DE BAJA RESERVA");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Reserva a dar de baja");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            if (ConsolaUtils.confirmar("¿Está seguro de dar de baja la reserva " + id + "?")) {
                reservaController.darDeBaja(id);
                ConsolaUtils.imprimirMensaje("Reserva dada de baja correctamente.");
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void reactivarReserva() {
        ConsolaUtils.imprimirTitulo("REACTIVAR RESERVA");
        String id = ConsolaUtils.leerTexto("Ingrese ID de Reserva a reactivar");
        if (ConsolaUtils.esCancelacion(id)) return;

        try {
            if (ConsolaUtils.confirmar("¿Está seguro de reactivar la reserva " + id + "?")) {
                reservaController.reactivar(id);
                ConsolaUtils.imprimirMensaje("Reserva reactivada correctamente.");
            }
        } catch (Exception e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }
}
