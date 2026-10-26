package sistemaMilitar.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sistemaMilitar.DAOs.AsignacionDAO;
import sistemaMilitar.DAOs.ICrud;
import sistemaMilitar.DAOs.PersonaDAO;
import sistemaMilitar.DTOs.AsignacionDTO;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.OperacionNoPermitidaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Asignacion;
import sistemaMilitar.models.Cuartel;
import sistemaMilitar.models.Oficial;
import sistemaMilitar.models.Persona;
import sistemaMilitar.utils.GeneradorCodigo;
import sistemaMilitar.utils.Validaciones;

public class AsignacionService {

    private final AsignacionDAO asignacionDAO;
    private final ICrud<Cuartel, String> cuartelDAO;
    private final PersonaDAO personaDAO;

    public AsignacionService(AsignacionDAO asignacionDAO, ICrud<Cuartel, String> cuartelDAO, PersonaDAO personaDAO) {
        this.asignacionDAO = asignacionDAO;
        this.cuartelDAO = cuartelDAO;
        this.personaDAO = personaDAO;
    }

    public String registrarAsignacion(String codigoCuartel, String idOficial) throws IOException {
        String codCuartelVal = Validaciones.validarCodigo(codigoCuartel, "CUA");
        String idOficialVal = Validaciones.normalizarTexto(idOficial);

        Cuartel cuartel = cuartelDAO.buscarPorId(codCuartelVal);
        if (cuartel == null || !cuartel.isActivo()) {
            throw new EntidadNoEncontradaException("El cuartel " + codCuartelVal + " no existe o está inactivo.");
        }

        Persona persona = personaDAO.buscarPorId(idOficialVal);
        if (persona == null || !(persona instanceof Oficial) || !persona.isActivo()) {
            throw new EntidadNoEncontradaException("El oficial " + idOficialVal + " no existe, no es un Oficial o está inactivo.");
        }

        Asignacion asgCuartel = asignacionDAO.buscarAsignacionActivaPorCuartel(codCuartelVal);
        if (asgCuartel != null) {
            throw new OperacionNoPermitidaException("El cuartel " + codCuartelVal + " ya tiene un oficial asignado (Asignación: " + asgCuartel.getId() + "). Relación 1 a 1.");
        }

        Asignacion asgOficial = asignacionDAO.buscarAsignacionActivaPorOficial(idOficialVal);
        if (asgOficial != null) {
            throw new OperacionNoPermitidaException("El oficial " + idOficialVal + " ya está asignado al cuartel " + asgOficial.getCodigoCuartel() + ". Relación 1 a 1.");
        }

        List<String> idsExistentes = asignacionDAO.obtenerTodosLosIds();
        String idGenerado = GeneradorCodigo.generarSiguienteId(idsExistentes, "ASG");

        Asignacion asignacion = new Asignacion(idGenerado, codCuartelVal, idOficialVal, true);
        asignacionDAO.agregar(asignacion);
        return idGenerado;
    }

    public List<AsignacionDTO> listar() {
        List<AsignacionDTO> dtos = new ArrayList<>();
        for (Asignacion a : asignacionDAO.listar()) {
            dtos.add(toDTO(a));
        }
        return dtos;
    }

    public AsignacionDTO buscarPorId(String id) {
        String idVal = Validaciones.normalizarTexto(id);
        Asignacion a = asignacionDAO.buscarPorId(idVal);
        if (a == null) {
            throw new EntidadNoEncontradaException("No existe la asignación con ID: " + idVal);
        }
        return toDTO(a);
    }

    public void darDeBaja(String id) throws IOException {
        String idVal = Validaciones.normalizarTexto(id);
        Asignacion a = asignacionDAO.buscarPorId(idVal);
        if (a == null) {
            throw new EntidadNoEncontradaException("No existe la asignación con ID: " + idVal);
        }
        if (!a.isActivo()) {
            throw new RegistroInvalidoException("La asignación ya está inactiva.");
        }
        asignacionDAO.eliminar(idVal);
    }

    public void reactivar(String id) throws IOException {
        String idVal = Validaciones.normalizarTexto(id);
        Asignacion a = asignacionDAO.buscarPorId(idVal);
        if (a == null) {
            throw new EntidadNoEncontradaException("No existe la asignación con ID: " + idVal);
        }
        if (a.isActivo()) {
            throw new RegistroInvalidoException("La asignación ya se encuentra activa.");
        }

        // Verificar regla 1 a 1 antes de reactivar
        Asignacion asgCuartel = asignacionDAO.buscarAsignacionActivaPorCuartel(a.getCodigoCuartel());
        if (asgCuartel != null) {
            throw new OperacionNoPermitidaException("No se puede reactivar: el cuartel ya posee otro oficial asignado.");
        }

        Asignacion asgOficial = asignacionDAO.buscarAsignacionActivaPorOficial(a.getIdOficial());
        if (asgOficial != null) {
            throw new OperacionNoPermitidaException("No se puede reactivar: el oficial ya está asignado a otro cuartel.");
        }

        asignacionDAO.reactivar(idVal);
    }

    private AsignacionDTO toDTO(Asignacion a) {
        return new AsignacionDTO(a.getId(), a.getCodigoCuartel(), a.getIdOficial(), a.isActivo());
    }
}
