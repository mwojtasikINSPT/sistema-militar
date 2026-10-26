package sistemaMilitar.views;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.controllers.PersonaController;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.PersistenciaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Oficial;
import sistemaMilitar.models.Persona;
import sistemaMilitar.utils.ConsolaUtils;

public class OficialView {

    private final PersonaController personaController;

    public OficialView(PersonaController personaController) {
        this.personaController = personaController;
    }

    public void mostrarMenu() {
        int opcion = -1;
        do {
            ConsolaUtils.imprimirMenu("GESTIÓN DE OFICIALES",
                    "1. Registrar Oficial",
                    "2. Listar Oficiales",
                    "3. Buscar Oficial por ID",
                    "4. Buscar Oficial por DNI",
                    "5. Dar de Baja (Baja Lógica)",
                    "6. Reactivar Oficial",
                    "0. Volver al Menú Principal"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    registrarOficial();
                    break;
                case 2:
                    listarOficiales();
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

    private void registrarOficial() {
        ConsolaUtils.imprimirTitulo("REGISTRAR OFICIAL");
        try {
            String dni = ConsolaUtils.leerTexto("Ingrese DNI (8 dígitos)");
            if (ConsolaUtils.esCancelacion(dni)) return;

            String nombre = ConsolaUtils.leerTexto("Ingrese Nombre");
            String apellido = ConsolaUtils.leerTexto("Ingrese Apellido");

            String idAsignado = personaController.registrarOficial(dni, nombre, apellido);
            ConsolaUtils.imprimirRegistroCreado("Oficial", idAsignado);
        } catch (EntidadDuplicadaException | RegistroInvalidoException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        } catch (PersistenciaException | IOException e) {
            ConsolaUtils.imprimirError("Error al guardar en archivo: " + e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void listarOficiales() {
        ConsolaUtils.imprimirTitulo("LISTADO DE OFICIALES");
        List<OficialDTO> oficiales = personaController.listarOficiales();
        if (oficiales.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay oficiales registrados.");
        } else {
            for (OficialDTO o : oficiales) {
                ConsolaUtils.imprimirMensaje(o.toString());
            }
        }
        ConsolaUtils.imprimirCantidad(oficiales.size());
        ConsolaUtils.pausar();
    }

    private void buscarPorId() {
        ConsolaUtils.imprimirTitulo("BUSCAR OFICIAL POR ID");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del Oficial (ej: OFI0001)");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Oficial)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Oficial.");
            } else {
                ConsolaUtils.imprimirMensaje(persona.toString());
            }
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void buscarPorDni() {
        ConsolaUtils.imprimirTitulo("BUSCAR OFICIAL POR DNI");
        try {
            String dni = ConsolaUtils.leerTexto("Ingrese DNI (8 dígitos)");
            if (ConsolaUtils.esCancelacion(dni)) return;

            Persona persona = personaController.buscarPorDni(dni);
            if (!(persona instanceof Oficial)) {
                ConsolaUtils.imprimirError("El DNI ingresado no corresponde a un Oficial.");
            } else {
                ConsolaUtils.imprimirMensaje(persona.toString());
            }
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void darDeBaja() {
        ConsolaUtils.imprimirTitulo("DAR DE BAJA OFICIAL");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del oficial a dar de baja");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Oficial)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Oficial.");
                ConsolaUtils.pausar();
                return;
            }

            if (ConsolaUtils.confirmar("¿Está seguro de dar de baja al oficial con ID " + id + "?")) {
                personaController.darDeBaja(id);
                ConsolaUtils.imprimirMensaje("Oficial dado de baja correctamente.");
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
        ConsolaUtils.imprimirTitulo("REACTIVAR OFICIAL");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID del oficial a reactivar");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            if (!(persona instanceof Oficial)) {
                ConsolaUtils.imprimirError("El ID ingresado no corresponde a un Oficial.");
                ConsolaUtils.pausar();
                return;
            }

            if (ConsolaUtils.confirmar("¿Está seguro de reactivar al oficial con ID " + id + "?")) {
                personaController.reactivar(id);
                ConsolaUtils.imprimirMensaje("Oficial reactivado correctamente.");
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
