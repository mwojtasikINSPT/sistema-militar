package sistemaMilitar.services;

import java.util.ArrayList;
import java.util.List;
import sistemaMilitar.DAOs.AsignacionDAO;
import sistemaMilitar.DAOs.CuartelDAO;
import sistemaMilitar.DAOs.PersonaDAO;
import sistemaMilitar.DAOs.ReservaDAO;
import sistemaMilitar.DTOs.OficialDTO;
import sistemaMilitar.DTOs.SoldadoDTO;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.OperacionNoPermitidaException;
import sistemaMilitar.models.Asignacion;
import sistemaMilitar.models.Cuartel;
import sistemaMilitar.models.Oficial;
import sistemaMilitar.models.Persona;
import sistemaMilitar.models.Reserva;
import sistemaMilitar.models.Soldado;
import sistemaMilitar.utils.Validaciones;

public class ConsultaService {

    private final PersonaDAO personaDAO;
    private final CuartelDAO cuartelDAO;
    private final ReservaDAO reservaDAO;
    private final AsignacionDAO asignacionDAO;

    public ConsultaService(PersonaDAO personaDAO, CuartelDAO cuartelDAO, ReservaDAO reservaDAO, AsignacionDAO asignacionDAO) {
        this.personaDAO = personaDAO;
        this.cuartelDAO = cuartelDAO;
        this.reservaDAO = reservaDAO;
        this.asignacionDAO = asignacionDAO;
    }

    /**
     * Consulta 1: El nombre del soldado con su cuartel asignado.
     */
    public String obtenerSoldadoConCuartel(String idODni) {
        String termino = Validaciones.normalizarTexto(idODni);
        Persona p = personaDAO.buscarPorId(termino);
        if (p == null) {
            p = personaDAO.buscarPorDni(termino);
        }

        if (p == null || !(p instanceof Soldado)) {
            throw new EntidadNoEncontradaException("No se encontró ningún soldado con ID/DNI: " + termino);
        }

        Soldado soldado = (Soldado) p;
        Reserva reserva = reservaDAO.buscarReservaActivaPorSoldado(soldado.getId());

        if (reserva == null) {
            return "Soldado: " + soldado.getApellido() + ", " + soldado.getNombre() + " (ID: " + soldado.getId() + ") | Estado Cuartel: SIN CUARTEL ASIGNADO";
        }

        Cuartel cuartel = cuartelDAO.buscarPorId(reserva.getCodigoCuartel());
        String infoCuartel = (cuartel != null) ? cuartel.getCodigo() + " (Capacidad: " + cuartel.getCapacidad() + ")" : reserva.getCodigoCuartel();

        return "Soldado: " + soldado.getApellido() + ", " + soldado.getNombre() + " (ID: " + soldado.getId() + ", DNI: " + soldado.getDni() + ") | Cuartel Asignado: " + infoCuartel;
    }

    /**
     * Consulta 2: Los soldados supervisados por un oficial.
     */
    public List<SoldadoDTO> obtenerSoldadosSupervisadosPorOficial(String idODniOficial) {
        String termino = Validaciones.normalizarTexto(idODniOficial);
        Persona p = personaDAO.buscarPorId(termino);
        if (p == null) {
            p = personaDAO.buscarPorDni(termino);
        }

        if (p == null || !(p instanceof Oficial)) {
            throw new EntidadNoEncontradaException("No se encontró ningún oficial con ID/DNI: " + termino);
        }

        Oficial oficial = (Oficial) p;
        Asignacion asignacion = asignacionDAO.buscarAsignacionActivaPorOficial(oficial.getId());

        if (asignacion == null) {
            throw new OperacionNoPermitidaException("El oficial " + oficial.getApellido() + ", " + oficial.getNombre() + " (ID: " + oficial.getId() + ") no tiene ningún cuartel asignado actualmente.");
        }

        String codigoCuartel = asignacion.getCodigoCuartel();
        List<SoldadoDTO> soldadosSupervisados = new ArrayList<>();

        for (Reserva r : reservaDAO.listar()) {
            if (r.isActivo() && r.getCodigoCuartel().equalsIgnoreCase(codigoCuartel)) {
                Persona pers = personaDAO.buscarPorId(r.getIdSoldado());
                if (pers instanceof Soldado && pers.isActivo()) {
                    Soldado s = (Soldado) pers;
                    soldadosSupervisados.add(new SoldadoDTO(s.getId(), s.getDni(), s.getNombre(), s.getApellido(), s.isActivo()));
                }
            }
        }

        return soldadosSupervisados;
    }

    /**
     * Consulta 3: El oficial asignado a un soldado.
     */
    public OficialDTO obtenerOficialAsignadoASoldado(String idODniSoldado) {
        String termino = Validaciones.normalizarTexto(idODniSoldado);
        Persona p = personaDAO.buscarPorId(termino);
        if (p == null) {
            p = personaDAO.buscarPorDni(termino);
        }

        if (p == null || !(p instanceof Soldado)) {
            throw new EntidadNoEncontradaException("No se encontró ningún soldado con ID/DNI: " + termino);
        }

        Soldado soldado = (Soldado) p;
        Reserva reserva = reservaDAO.buscarReservaActivaPorSoldado(soldado.getId());

        if (reserva == null) {
            throw new OperacionNoPermitidaException("El soldado " + soldado.getApellido() + ", " + soldado.getNombre() + " (ID: " + soldado.getId() + ") no posee un cuartel asignado.");
        }

        String codigoCuartel = reserva.getCodigoCuartel();
        Asignacion asignacion = asignacionDAO.buscarAsignacionActivaPorCuartel(codigoCuartel);

        if (asignacion == null) {
            throw new OperacionNoPermitidaException("El cuartel (" + codigoCuartel + ") donde reside el soldado no tiene un oficial asignado a cargo.");
        }

        Persona persOficial = personaDAO.buscarPorId(asignacion.getIdOficial());
        if (persOficial == null || !(persOficial instanceof Oficial)) {
            throw new EntidadNoEncontradaException("No se encontraron los datos del oficial con ID: " + asignacion.getIdOficial());
        }

        Oficial of = (Oficial) persOficial;
        return new OficialDTO(of.getId(), of.getDni(), of.getNombre(), of.getApellido(), of.isActivo());
    }
}
