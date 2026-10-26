package sistemaMilitar.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sistemaMilitar.DAOs.PersonaDAO;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Oficial;
import sistemaMilitar.models.Persona;
import sistemaMilitar.models.Soldado;
import sistemaMilitar.utils.GeneradorCodigo;
import sistemaMilitar.utils.Validaciones;

public class PersonaService {

    private final PersonaDAO personaDAO;

    public PersonaService(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    public String registrarSoldado(String dni, String nombre, String apellido) throws IOException {
        String dniValidado = Validaciones.validarDni(dni);
        if (personaDAO.buscarPorDni(dniValidado) != null) {
            throw new EntidadDuplicadaException("El DNI " + dniValidado + " ya pertenece a una persona registrada.");
        }

        String nombreValidado = Validaciones.validarTextoObligatorio(nombre, "Nombre");
        String apellidoValidado = Validaciones.validarTextoObligatorio(apellido, "Apellido");

        List<String> idsExistentes = personaDAO.obtenerTodosLosIds();
        String idGenerado = GeneradorCodigo.generarSiguienteId(idsExistentes, "SOL");

        Soldado soldado = new Soldado(idGenerado, dniValidado, nombreValidado, apellidoValidado, true);
        personaDAO.agregar(soldado);
        return idGenerado;
    }

    public String registrarOficial(String dni, String nombre, String apellido) throws IOException {
        String dniValidado = Validaciones.validarDni(dni);
        if (personaDAO.buscarPorDni(dniValidado) != null) {
            throw new EntidadDuplicadaException("El DNI " + dniValidado + " ya pertenece a una persona registrada.");
        }

        String nombreValidado = Validaciones.validarTextoObligatorio(nombre, "Nombre");
        String apellidoValidado = Validaciones.validarTextoObligatorio(apellido, "Apellido");

        List<String> idsExistentes = personaDAO.obtenerTodosLosIds();
        String idGenerado = GeneradorCodigo.generarSiguienteId(idsExistentes, "OFI");

        Oficial oficial = new Oficial(idGenerado, dniValidado, nombreValidado, apellidoValidado, true);
        personaDAO.agregar(oficial);
        return idGenerado;
    }

    public List<SoldadoDTO> listarSoldados() {
        List<SoldadoDTO> resultado = new ArrayList<>();
        for (Persona p : personaDAO.listar()) {
            if (p instanceof Soldado) {
                resultado.add(new SoldadoDTO(p.getId(), p.getDni(), p.getNombre(), p.getApellido(), p.isActivo()));
            }
        }
        return resultado;
    }

    public List<OficialDTO> listarOficiales() {
        List<OficialDTO> resultado = new ArrayList<>();
        for (Persona p : personaDAO.listar()) {
            if (p instanceof Oficial) {
                resultado.add(new OficialDTO(p.getId(), p.getDni(), p.getNombre(), p.getApellido(), p.isActivo()));
            }
        }
        return resultado;
    }

    public Persona buscarPorId(String id) {
        String idNormalizado = Validaciones.normalizarTexto(id);
        Persona p = personaDAO.buscarPorId(idNormalizado);
        if (p == null) {
            throw new EntidadNoEncontradaException("No existe la persona con ID: " + idNormalizado);
        }
        return p;
    }

    public Persona buscarPorDni(String dni) {
        String dniValidado = Validaciones.validarDni(dni);
        Persona p = personaDAO.buscarPorDni(dniValidado);
        if (p == null) {
            throw new EntidadNoEncontradaException("No existe la persona con DNI: " + dniValidado);
        }
        return p;
    }

    public void darDeBaja(String id) throws IOException {
        String idNormalizado = Validaciones.normalizarTexto(id);
        Persona p = personaDAO.buscarPorId(idNormalizado);
        if (p == null) {
            throw new EntidadNoEncontradaException("No existe la persona con ID: " + idNormalizado);
        }
        if (!p.isActivo()) {
            throw new RegistroInvalidoException("La persona ya se encuentra dada de baja.");
        }
        personaDAO.eliminar(idNormalizado);
    }

    public void reactivar(String id) throws IOException {
        String idNormalizado = Validaciones.normalizarTexto(id);
        Persona p = personaDAO.buscarPorId(idNormalizado);
        if (p == null) {
            throw new EntidadNoEncontradaException("No existe la persona con ID: " + idNormalizado);
        }
        if (p.isActivo()) {
            throw new RegistroInvalidoException("La persona ya se encuentra activa.");
        }
        personaDAO.reactivar(idNormalizado);
    }
}
