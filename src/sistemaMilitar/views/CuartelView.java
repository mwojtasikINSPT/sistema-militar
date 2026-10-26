package sistemaMilitar.views;

import java.util.List;
import sistemaMilitar.controllers.CuartelController;
import sistemaMilitar.DTOs.CuartelDTO;
import sistemaMilitar.utils.ConsolaUtils;

public class CuartelView {

    private final CuartelController controller;

    public CuartelView(CuartelController controller) {
        this.controller = controller;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE CUARTELES ---");
            System.out.println("1. Agregar Cuartel");
            System.out.println("2. Listar Cuarteles");
            System.out.println("3. Buscar Cuartel por Código");
            System.out.println("4. Actualizar Cuartel");
            System.out.println("5. Eliminar (Baja Lógica) Cuartel");
            System.out.println("6. Reactivar Cuartel");
            System.out.println("0. Volver al Menú Principal");

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        agregar();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        buscarPorId();
                        break;
                    case 4:
                        actualizar();
                        break;
                    case 5:
                        eliminar();
                        break;
                    case 6:
                        reactivar();
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void agregar() throws Exception {
        System.out.println("\n--- AGREGAR CUARTEL ---");
        String codigo = ConsolaUtils.leerTexto("Ingrese código del cuartel (ej. CUA0001)");
        int capacidad = ConsolaUtils.leerEntero("Ingrese capacidad máxima");

        CuartelDTO dto = new CuartelDTO(codigo, capacidad);
        controller.agregar(dto);
        System.out.println("¡Cuartel agregado exitosamente!");
    }

    private void listar() {
        System.out.println("\n--- LISTADO DE CUARTELES ---");
        List<CuartelDTO> lista = controller.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay cuarteles registrados.");
        } else {
            for (CuartelDTO c : lista) {
                System.out.println(c);
            }
        }
    }

    private void buscarPorId() {
        System.out.println("\n--- BUSCAR CUARTEL ---");
        String codigo = ConsolaUtils.leerTexto("Ingrese código del cuartel a buscar");
        CuartelDTO c = controller.buscarPorId(codigo);
        System.out.println("Resultado: " + c);
    }

    private void actualizar() throws Exception {
        System.out.println("\n--- ACTUALIZAR CUARTEL ---");
        String codigo = ConsolaUtils.leerTexto("Ingrese código del cuartel a actualizar");
        CuartelDTO existente = controller.buscarPorId(codigo);
        System.out.println("Datos actuales: " + existente);

        int nuevaCapacidad = ConsolaUtils.leerEntero("Ingrese nueva capacidad");
        CuartelDTO dto = new CuartelDTO(codigo, nuevaCapacidad, existente.isActivo());
        controller.actualizar(dto);
        System.out.println("¡Cuartel actualizado exitosamente!");
    }

    private void eliminar() throws Exception {
        System.out.println("\n--- BAJA LÓGICA DE CUARTEL ---");
        String codigo = ConsolaUtils.leerTexto("Ingrese código del cuartel a dar de baja");
        CuartelDTO c = controller.buscarPorId(codigo);
        System.out.println("Cuartel seleccionado: " + c);

        if (ConsolaUtils.confirmar("¿Está seguro de dar de baja este cuartel?")) {
            controller.eliminar(codigo);
            System.out.println("¡Cuartel dado de baja exitosamente!");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void reactivar() throws Exception {
        System.out.println("\n--- REACTIVAR CUARTEL ---");
        String codigo = ConsolaUtils.leerTexto("Ingrese código del cuartel a reactivar");
        CuartelDTO c = controller.buscarPorId(codigo);
        System.out.println("Cuartel seleccionado: " + c);

        if (ConsolaUtils.confirmar("¿Desea reactivar este cuartel?")) {
            controller.reactivar(codigo);
            System.out.println("¡Cuartel reactivado exitosamente!");
        } else {
            System.out.println("Operación cancelada.");
        }
    }
}
