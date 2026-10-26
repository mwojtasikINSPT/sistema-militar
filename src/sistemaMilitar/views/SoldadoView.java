package sistemaMilitar.views;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.controllers.PersonaController;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.PersistenciaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Persona;
import sistemaMilitar.models.Soldado;
import sistemaMilitar.utils.ConsolaUtils;

public class SoldadoView {

    private final PersonaController personaController;

    public SoldadoView(PersonaController personaController) {
        this.personaController = personaController;
    }

    public void mostrarMenu() {
        int opcion = -1;
        do {
            ConsolaUtils.imprimirMenu("GESTIÓN DE SOLDADOS",
                    "1. Registrar Soldado",
                    "2. Listar Soldados",
                    "3. Buscar Soldado por ID",
                    "4. Buscar Soldado por DNI",
                    "5. Dar de Baja (Baja Lógica)",
                    "6. Reactivar Soldado",
                    "0. Volver al Menú Principal"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    registrarSoldado();
                    break;
                case 2:
                    listarSoldados();
                    break;
                case 3:
                    buscarPorId();
                    break;
                case 4:
                    buscarPorDni();
                    break;
                case 5:
                    darDeBaja();
                    break;
                case 6:
                    reactivar();
                    break;
                case 0:
                    ConsolaUtils.imprimirMensaje("Volviendo al menú principal...");
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    private void registrarSoldado() {
        ConsolaUtils.imprimirTitulo("REGISTRAR SOLDADO");
        try {
            String dni = ConsolaUtils.leerTexto("Ingrese DNI (8 dígitos)");
            if (ConsolaUtils.esCancelacion(dni)) return;

            String nombre = ConsolaUtils.leerTexto("Ingrese Nombre");
            String apellido = ConsolaUtils.leerTexto("Ingrese Apellido");

            String idAsignado = personaController.registrarSoldado(dni, nombre, apellido);
            ConsolaUtils.imprimirRegistroCreado("Soldado", idAsignado);
        } catch (EntidadDuplicadaException | RegistroInvalidoException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        } catch (PersistenciaException | IOException e) {
            ConsolaUtils.imprimirError("Error al guardar en archivo: " + e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void listarSoldados() {
        ConsolaUtils.imprimirTitulo("LISTADO DE SOLDADOS");
        List<SoldadoDTO> soldados = personaController.listarSoldados();
        if (soldados.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay soldados registrados.");
        } else {
            for (SoldadoDTO s : soldados) {
                ConsolaUtils.imprimirMensaje(s.toString());
            }
        }
        ConsolaUtils.imprimirCantidad(soldados.size());
        ConsolaUtils.pausar();
    }

    private void buscarPorId() {
        ConsolaUtils.imprimirTitulo("BUSCAR SOLDADO POR ID");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del Soldado (ej: SOL0001)");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Soldado)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Soldado.");
            } else {
                ConsolaUtils.imprimirMensaje(persona.toString());
            }
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void buscarPorDni() {
        ConsolaUtils.imprimirTitulo("BUSCAR SOLDADO POR DNI");
        try {
            String dni = ConsolaUtils.leerTexto("Ingrese DNI (8 dígitos)");
            if (ConsolaUtils.esCancelacion(dni)) return;

            Persona persona = personaController.buscarPorDni(dni);
            if (!(persona instanceof Soldado)) {
                ConsolaUtils.imprimirError("El DNI ingresado no corresponde a un Soldado.");
            } else {
                ConsolaUtils.imprimirMensaje(persona.toString());
            }
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void darDeBaja() {
        ConsolaUtils.imprimirTitulo("DAR DE BAJA SOLDADO");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del soldado a dar de baja");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Soldado)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Soldado.");
                ConsolaUtils.pausar();
                return;
            }

            if (ConsolaUtils.confirmar("¿Está seguro de dar de baja al soldado con ID " + id + "?")) {
                personaController.darDeBaja(id);
                ConsolaUtils.imprimirMensaje("Soldado dado de baja correctamente.");
            } else {
                ConsolaUtils.imprimirMensaje("Operación cancelada.");
            }
        } catch (EntidadNoEncontradaException | RegistroInvalidoException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        } catch (PersistenciaException | IOException e) {
            ConsolaUtils.imprimirError("Error al actualizar archivo: " + e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void reactivar() {
        ConsolaUtils.imprimirTitulo("REACTIVAR SOLDADO");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del soldado a reactivar");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Soldado)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Soldado.");
                ConsolaUtils.pausar();
                return;
            }

            if (ConsolaUtils.confirmar("¿Está seguro de reactivar al soldado con ID " + id + "?")) {
                personaController.reactivar(id);
                ConsolaUtils.imprimirMensaje("Soldado reactivado correctamente.");
            } else {
                ConsolaUtils.imprimirMensaje("Operación cancelada.");
            }
        } catch (EntidadNoEncontradaException | RegistroInvalidoException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        } catch (PersistenciaException | IOException e) {
            ConsolaUtils.imprimirError("Error al actualizar archivo: " + e.getMessage());
        }
        ConsolaUtils.pausar();
    }
}
