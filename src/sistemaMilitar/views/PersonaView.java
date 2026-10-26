package sistemaMilitar.views;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.controllers.PersonaController;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.PersistenciaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Persona;
import sistemaMilitar.utils.ConsolaUtils;

public class PersonaView {

    private final PersonaController personaController;

    public PersonaView(PersonaController personaController) {
        this.personaController = personaController;
    }

    public void mostrarMenu() {
        int opcion = -1;
        do {
            ConsolaUtils.imprimirMenu("GESTIÓN DE PERSONAS (SOLDADOS Y OFICIALES)",
                    "1. Registrar Soldado",
                    "2. Registrar Oficial",
                    "3. Listar Soldados",
                    "4. Listar Oficiales",
                    "5. Buscar por ID",
                    "6. Buscar por DNI",
                    "7. Dar de Baja (Baja Lógica)",
                    "8. Reactivar Persona",
                    "0. Volver al Menú Principal"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    registrarSoldado();
                    break;
                case 2:
                    registrarOficial();
                    break;
                case 3:
                    listarSoldados();
                    break;
                case 4:
                    listarOficiales();
                    break;
                case 5:
                    buscarPorId();
                    break;
                case 6:
                    buscarPorDni();
                    break;
                case 7:
                    darDeBaja();
                    break;
                case 8:
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
        ConsolaUtils.imprimirTitulo("BUSCAR PERSONA POR ID");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID (ej: SOL0001 / OFI0001)");
            if (ConsolaUtils.esCancelacion(id)) return;

            Persona persona = personaController.buscarPorId(id);
            ConsolaUtils.imprimirMensaje(persona.toString());
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void buscarPorDni() {
        ConsolaUtils.imprimirTitulo("BUSCAR PERSONA POR DNI");
        try {
            String dni = ConsolaUtils.leerTexto("Ingrese DNI (8 dígitos)");
            if (ConsolaUtils.esCancelacion(dni)) return;

            Persona persona = personaController.buscarPorDni(dni);
            ConsolaUtils.imprimirMensaje(persona.toString());
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            ConsolaUtils.imprimirError(e.getMessage());
        }
        ConsolaUtils.pausar();
    }

    private void darDeBaja() {
        ConsolaUtils.imprimirTitulo("DAR DE BAJA PERSONA");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID de la persona a dar de baja");
            if (ConsolaUtils.esCancelacion(id)) return;

            if (ConsolaUtils.confirmar("¿Está seguro de dar de baja la persona con ID " + id + "?")) {
                personaController.darDeBaja(id);
                ConsolaUtils.imprimirMensaje("Persona dada de baja correctamente.");
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
        ConsolaUtils.imprimirTitulo("REACTIVAR PERSONA");
        try {
            String id = ConsolaUtils.leerTexto("Ingrese ID de la persona a reactivar");
            if (ConsolaUtils.esCancelacion(id)) return;

            if (ConsolaUtils.confirmar("¿Está seguro de reactivar la persona con ID " + id + "?")) {
                personaController.reactivar(id);
                ConsolaUtils.imprimirMensaje("Persona reactivada correctamente.");
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
