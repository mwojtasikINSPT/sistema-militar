package sistemaMilitar.controllers;

import java.io.IOException;
import java.util.List;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.models.Persona;
import sistemaMilitar.services.PersonaService;

public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    public String registrarSoldado(String dni, String nombre, String apellido) throws IOException {
        return personaService.registrarSoldado(dni, nombre, apellido);
    }

    public String registrarOficial(String dni, String nombre, String apellido) throws IOException {
        return personaService.registrarOficial(dni, nombre, apellido);
    }

    public List<SoldadoDTO> listarSoldados() {
        return personaService.listarSoldados();
    }

    public List<OficialDTO> listarOficiales() {
        return personaService.listarOficiales();
    }

    public Persona buscarPorId(String id) {
        return personaService.buscarPorId(id);
    }

    public Persona buscarPorDni(String dni) {
        return personaService.buscarPorDni(dni);
    }

    public void darDeBaja(String id) throws IOException {
        personaService.darDeBaja(id);
    }

    public void reactivar(String id) throws IOException {
        personaService.reactivar(id);
    }
}
