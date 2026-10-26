package sistemaMilitar.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sistemaMilitar.DAOs.ICrud;
import sistemaMilitar.DAOs.PersonaDAO;
import sistemaMilitar.DAOs.ReservaDAO;
import sistemaMilitar.DTOs.ReservaDTO;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.OperacionNoPermitidaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Cuartel;
import sistemaMilitar.models.Persona;
import sistemaMilitar.models.Reserva;
import sistemaMilitar.models.Soldado;
import sistemaMilitar.utils.GeneradorCodigo;
import sistemaMilitar.utils.Validaciones;

public class ReservaService {

    private final ReservaDAO reservaDAO;
    private final ICrud<Cuartel, String> cuartelDAO;
    private final PersonaDAO personaDAO;

    public ReservaService(ReservaDAO reservaDAO, ICrud<Cuartel, String> cuartelDAO, PersonaDAO personaDAO) {
        this.reservaDAO = reservaDAO;
        this.cuartelDAO = cuartelDAO;
        this.personaDAO = personaDAO;
    }

    public String registrarReserva(String codigoCuartel, String idSoldado) throws IOException {
        String codCuartelVal = Validaciones.validarCodigo(codigoCuartel, "CUA");
        String idSoldadoVal = Validaciones.normalizarTexto(idSoldado);

        Cuartel cuartel = cuartelDAO.buscarPorId(codCuartelVal);
        if (cuartel == null || !cuartel.isActivo()) {
            throw new EntidadNoEncontradaException("El cuartel " + codCuartelVal + " no existe o está inactivo.");
        }

        Persona persona = personaDAO.buscarPorId(idSoldadoVal);
        if (persona == null || !(persona instanceof Soldado) || !persona.isActivo()) {
            throw new EntidadNoEncontradaException("El soldado " + idSoldadoVal + " no existe, no es un Soldado o está inactivo.");
        }

        Reserva reservaExistente = reservaDAO.buscarReservaActivaPorSoldado(idSoldadoVal);
        if (reservaExistente != null) {
            throw new EntidadDuplicadaException("El soldado " + idSoldadoVal + " ya tiene una reserva activa en el cuartel " + reservaExistente.getCodigoCuartel());
        }

        int ocupacionActual = reservaDAO.contarSoldadosActivosEnCuartel(codCuartelVal);
        if (ocupacionActual >= cuartel.getCapacidad()) {
            throw new OperacionNoPermitidaException("El cuartel " + codCuartelVal + " ha alcanzado su capacidad máxima (" + cuartel.getCapacidad() + " soldados).");
        }

        List<String> idsExistentes = reservaDAO.obtenerTodosLosIds();
        String idGenerado = GeneradorCodigo.generarSiguienteId(idsExistentes, "RES");

        Reserva reserva = new Reserva(idGenerado, codCuartelVal, idSoldadoVal, true);
        reservaDAO.agregar(reserva);
        return idGenerado;
    }

    public List<ReservaDTO> listar() {
        List<ReservaDTO> dtos = new ArrayList<>();
        for (Reserva r : reservaDAO.listar()) {
            dtos.add(toDTO(r));
        }
        return dtos;
    }

    public ReservaDTO buscarPorId(String id) {
        String idVal = Validaciones.normalizarTexto(id);
        Reserva r = reservaDAO.buscarPorId(idVal);
        if (r == null) {
            throw new EntidadNoEncontradaException("No existe la reserva con ID: " + idVal);
        }
        return toDTO(r);
    }

    public void darDeBaja(String id) throws IOException {
        String idVal = Validaciones.normalizarTexto(id);
        Reserva r = reservaDAO.buscarPorId(idVal);
        if (r == null) {
            throw new EntidadNoEncontradaException("No existe la reserva con ID: " + idVal);
        }
        if (!r.isActivo()) {
            throw new RegistroInvalidoException("La reserva ya está inactiva.");
        }
        reservaDAO.eliminar(idVal);
    }

    public void reactivar(String id) throws IOException {
        String idVal = Validaciones.normalizarTexto(id);
        Reserva r = reservaDAO.buscarPorId(idVal);
        if (r == null) {
            throw new EntidadNoEncontradaException("No existe la reserva con ID: " + idVal);
        }
        if (r.isActivo()) {
            throw new RegistroInvalidoException("La reserva ya se encuentra activa.");
        }

        // Verificar capacidad del cuartel antes de reactivar
        Cuartel cuartel = cuartelDAO.buscarPorId(r.getCodigoCuartel());
        if (cuartel == null || !cuartel.isActivo()) {
            throw new OperacionNoPermitidaException("No se puede reactivar: el cuartel asociado no existe o está inactivo.");
        }
        int ocupacionActual = reservaDAO.contarSoldadosActivosEnCuartel(r.getCodigoCuartel());
        if (ocupacionActual >= cuartel.getCapacidad()) {
            throw new OperacionNoPermitidaException("No se puede reactivar: el cuartel ha alcanzado su capacidad máxima.");
        }

        reservaDAO.reactivar(idVal);
    }

    private ReservaDTO toDTO(Reserva r) {
        return new ReservaDTO(r.getId(), r.getCodigoCuartel(), r.getIdSoldado(), r.isActivo());
    }
}
