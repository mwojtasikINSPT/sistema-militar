package sistemaMilitar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import sistemaMilitar.DAOs.ICrud;
import sistemaMilitar.DTOs.CuartelDTO;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.RegistroInvalidoException;
import sistemaMilitar.models.Cuartel;
import sistemaMilitar.utils.GeneradorCodigo;
import sistemaMilitar.utils.Validaciones;

public class CuartelService {

    private final ICrud<Cuartel, String> cuartelDAO;

    public CuartelService(ICrud<Cuartel, String> cuartelDAO) {
        this.cuartelDAO = cuartelDAO;
    }

    public String agregar(CuartelDTO dto) {
        if (dto.getCapacidad() <= 0) {
            throw new RegistroInvalidoException("La capacidad del cuartel debe ser mayor a cero.");
        }

        List<String> idsExistentes = cuartelDAO.listar().stream()
                .map(Cuartel::getCodigo)
                .collect(Collectors.toList());

        String idGenerado = GeneradorCodigo.generarSiguienteId(idsExistentes, "CUA");
        dto.setCodigo(idGenerado);

        Cuartel cuartel = toEntity(dto);
        try {
            cuartelDAO.agregar(cuartel);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el cuartel en persistencia", e);
        }
        return idGenerado;
    }

    public List<CuartelDTO> listar() {
        List<Cuartel> cuarteles = cuartelDAO.listar();
        List<CuartelDTO> dtos = new ArrayList<>();
        for (Cuartel c : cuarteles) {
            dtos.add(toDTO(c));
        }
        return dtos;
    }

    public CuartelDTO buscarPorId(String codigo) {
        String codigoValido = Validaciones.validarCodigo(codigo, "CUA");
        Cuartel c = cuartelDAO.buscarPorId(codigoValido);
        if (c == null) {
            throw new EntidadNoEncontradaException("No se encontró el cuartel con código: " + codigoValido);
        }
        return toDTO(c);
    }

    public void actualizar(CuartelDTO dto) {
        String codigoValido = Validaciones.validarCodigo(dto.getCodigo(), "CUA");
        Cuartel existente = cuartelDAO.buscarPorId(codigoValido);
        if (existente == null) {
            throw new EntidadNoEncontradaException("No existe el cuartel que desea actualizar: " + codigoValido);
        }
        if (dto.getCapacidad() <= 0) {
            throw new RegistroInvalidoException("La capacidad del cuartel debe ser mayor a cero.");
        }
        Cuartel cuartel = toEntity(dto);
        try {
            cuartelDAO.actualizar(cuartel);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cuartel", e);
        }
    }

    public void eliminar(String codigo) {
        String codigoValido = Validaciones.validarCodigo(codigo, "CUA");
        Cuartel existente = cuartelDAO.buscarPorId(codigoValido);
        if (existente == null) {
            throw new EntidadNoEncontradaException("No existe el cuartel con código: " + codigoValido);
        }
        try {
            cuartelDAO.eliminar(codigoValido);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cuartel", e);
        }
    }

    public void reactivar(String codigo) {
        String codigoValido = Validaciones.validarCodigo(codigo, "CUA");
        Cuartel existente = cuartelDAO.buscarPorId(codigoValido);
        if (existente == null) {
            throw new EntidadNoEncontradaException("No existe el cuartel con código: " + codigoValido);
        }
        try {
            cuartelDAO.reactivar(codigoValido);
        } catch (Exception e) {
            throw new RuntimeException("Error al reactivar el cuartel", e);
        }
    }

    private Cuartel toEntity(CuartelDTO dto) {
        return new Cuartel(dto.getCodigo(), dto.getCapacidad(), dto.isActivo());
    }

    private CuartelDTO toDTO(Cuartel entity) {
        return new CuartelDTO(entity.getCodigo(), entity.getCapacidad(), entity.isActivo());
    }
}
