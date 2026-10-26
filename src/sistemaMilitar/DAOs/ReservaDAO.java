package sistemaMilitar.DAOs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sistemaMilitar.exceptions.EntidadDuplicadaException;
import sistemaMilitar.exceptions.EntidadNoEncontradaException;
import sistemaMilitar.exceptions.PersistenciaException;
import sistemaMilitar.models.Reserva;

public class ReservaDAO implements ICrud<Reserva, String> {

    private final String archivoPath = "reservas.txt";
    private final List<Reserva> cache = new ArrayList<>();

    public ReservaDAO() {
        cargarDesdeArchivo();
    }

    private void cargarDesdeArchivo() {
        cache.clear();
        File archivo = new File(archivoPath);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                throw new PersistenciaException("Error al crear el archivo " + archivoPath, e);
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    String id = partes[0].trim();
                    String codigoCuartel = partes[1].trim();
                    String idSoldado = partes[2].trim();
                    boolean activo = Boolean.parseBoolean(partes[3].trim());

                    cache.add(new Reserva(id, codigoCuartel, idSoldado, activo));
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de reservas", e);
        }
    }

    private void guardarEnArchivo() throws IOException {
        File archivo = new File(archivoPath);
        File temp = new File(archivoPath + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
            for (Reserva r : cache) {
                writer.write(r.getId() + "," + r.getCodigoCuartel() + "," + r.getIdSoldado() + "," + r.isActivo());
                writer.newLine();
            }
        } catch (IOException e) {
            if (temp.exists()) {
                temp.delete();
            }
            throw new PersistenciaException("Error al escribir el archivo de reservas", e);
        }

        if (archivo.exists() && !archivo.delete()) {
            throw new IOException("No se pudo reemplazar el archivo " + archivoPath);
        }
        if (!temp.renameTo(archivo)) {
            throw new IOException("No se pudo renombrar el archivo temporal de reservas");
        }
    }

    @Override
    public void agregar(Reserva entidad) throws IOException {
        if (buscarPorId(entidad.getId()) != null) {
            throw new EntidadDuplicadaException("Ya existe una reserva con ID: " + entidad.getId());
        }
        cache.add(entidad);
        guardarEnArchivo();
    }

    @Override
    public List<Reserva> listar() {
        return new ArrayList<>(cache);
    }

    @Override
    public Reserva buscarPorId(String id) {
        for (Reserva r : cache) {
            if (r.getId().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Reserva entidad) throws IOException {
        int index = -1;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(entidad.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntidadNoEncontradaException("No se encontró la reserva con ID: " + entidad.getId());
        }

        cache.set(index, entidad);
        guardarEnArchivo();
    }

    @Override
    public void eliminar(String id) throws IOException {
        Reserva r = buscarPorId(id);
        if (r == null) {
            throw new EntidadNoEncontradaException("No se encontró la reserva con ID: " + id);
        }
        r.setActivo(false);
        guardarEnArchivo();
    }

    @Override
    public void reactivar(String id) throws IOException {
        Reserva r = buscarPorId(id);
        if (r == null) {
            throw new EntidadNoEncontradaException("No se encontró la reserva con ID: " + id);
        }
        r.setActivo(true);
        guardarEnArchivo();
    }

    public List<String> obtenerTodosLosIds() {
        List<String> ids = new ArrayList<>();
        for (Reserva r : cache) {
            ids.add(r.getId());
        }
        return ids;
    }

    public int contarSoldadosActivosEnCuartel(String codigoCuartel) {
        int cont = 0;
        for (Reserva r : cache) {
            if (r.isActivo() && r.getCodigoCuartel().equalsIgnoreCase(codigoCuartel)) {
                cont++;
            }
        }
        return cont;
    }

    public Reserva buscarReservaActivaPorSoldado(String idSoldado) {
        for (Reserva r : cache) {
            if (r.isActivo() && r.getIdSoldado().equalsIgnoreCase(idSoldado)) {
                return r;
            }
        }
        return null;
    }
}
