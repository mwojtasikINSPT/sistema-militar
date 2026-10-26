package sistemaMilitar.views;

import sistemaMilitar.utils.ConsolaUtils;

public class MenuPrincipalView {

    private final CuartelView cuartelView;
    private final SoldadoView soldadoView;
    private final OficialView oficialView;
    private final ReservaView reservaView;
    private final AsignacionView asignacionView;
    private final ConsultaView consultaView;

    public MenuPrincipalView(CuartelView cuartelView,
                              SoldadoView soldadoView,
                              OficialView oficialView,
                              ReservaView reservaView,
                              AsignacionView asignacionView,
                              ConsultaView consultaView) {
        this.cuartelView = cuartelView;
        this.soldadoView = soldadoView;
        this.oficialView = oficialView;
        this.reservaView = reservaView;
        this.asignacionView = asignacionView;
        this.consultaView = consultaView;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtils.imprimirMenu(
                    "SISTEMA DE GESTIÓN MILITAR - MENÚ PRINCIPAL",
                    "1. Gestión de Cuarteles",
                    "2. Gestión de Soldados",
                    "3. Gestión de Oficiales",
                    "4. Gestión de Reservas (Cuarteles <-> Soldados 1:N)",
                    "5. Gestión de Asignaciones (Oficiales <-> Cuarteles 1:1)",
                    "6. Consultas Obligatorias del Sistema",
                    "0. Salir del Sistema"
            );

            int opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    cuartelView.mostrarMenu();
                    break;
                case 2:
                    soldadoView.mostrarMenu();
                    break;
                case 3:
                    oficialView.mostrarMenu();
                    break;
                case 4:
                    reservaView.mostrarMenu();
                    break;
                case 5:
                    asignacionView.mostrarMenu();
                    break;
                case 6:
                    consultaView.mostrarMenu();
                    break;
                case 0:
                    ConsolaUtils.imprimirMensaje("Gracias por utilizar el Sistema de Gestión Militar. ¡Hasta luego!");
                    salir = true;
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción inválida. Por favor, reintente.");
            }
        }
    }
}
