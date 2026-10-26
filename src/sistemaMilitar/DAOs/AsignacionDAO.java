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
import sistemaMilitar.models.Asignacion;

public class AsignacionDAO implements ICrud<Asignacion, String> {

    private final String archivoPath = "asignaciones.txt";
    private final List<Asignacion> cache = new ArrayList<>();

    public AsignacionDAO() {
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
                    String idOficial = partes[2].trim();
                    boolean activo = Boolean.parseBoolean(partes[3].trim());

                    cache.add(new Asignacion(id, codigoCuartel, idOficial, activo));
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de asignaciones", e);
        }
    }

    private void guardarEnArchivo() throws IOException {
        File archivo = new File(archivoPath);
        File temp = new File(archivoPath + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
            for (Asignacion a : cache) {
                writer.write(a.getId() + "," + a.getCodigoCuartel() + "," + a.getIdOficial() + "," + a.isActivo());
                writer.newLine();
            }
        } catch (IOException e) {
            if (temp.exists()) {
                temp.delete();
            }
            throw new PersistenciaException("Error al escribir el archivo de asignaciones", e);
        }

        if (archivo.exists() && !archivo.delete()) {
            throw new IOException("No se pudo reemplazar el archivo " + archivoPath);
        }
        if (!temp.renameTo(archivo)) {
            throw new IOException("No se pudo renombrar el archivo temporal de asignaciones");
        }
    }

    @Override
    public void agregar(Asignacion entidad) throws IOException {
        if (buscarPorId(entidad.getId()) != null) {
            throw new EntidadDuplicadaException("Ya existe una asignación con ID: " + entidad.getId());
        }
        cache.add(entidad);
        guardarEnArchivo();
    }

    @Override
    public List<Asignacion> listar() {
        return new ArrayList<>(cache);
    }

    @Override
    public Asignacion buscarPorId(String id) {
        for (Asignacion a : cache) {
            if (a.getId().equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Asignacion entidad) throws IOException {
        int index = -1;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(entidad.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntidadNoEncontradaException("No se encontró la asignación con ID: " + entidad.getId());
        }

        cache.set(index, entidad);
        guardarEnArchivo();
    }

    @Override
    public void eliminar(String id) throws IOException {
        Asignacion a = buscarPorId(id);
        if (a == null) {
            throw new EntidadNoEncontradaException("No se encontró la asignación con ID: " + id);
        }
        a.setActivo(false);
        guardarEnArchivo();
    }

    @Override
    public void reactivar(String id) throws IOException {
        Asignacion a = buscarPorId(id);
        if (a == null) {
            throw new EntidadNoEncontradaException("No se encontró la asignación con ID: " + id);
        }
        a.setActivo(true);
        guardarEnArchivo();
    }

    public List<String> obtenerTodosLosIds() {
        List<String> ids = new ArrayList<>();
        for (Asignacion a : cache) {
            ids.add(a.getId());
        }
        return ids;
    }

    public Asignacion buscarAsignacionActivaPorCuartel(String codigoCuartel) {
        for (Asignacion a : cache) {
            if (a.isActivo() && a.getCodigoCuartel().equalsIgnoreCase(codigoCuartel)) {
                return a;
            }
        }
        return null;
    }

    public Asignacion buscarAsignacionActivaPorOficial(String idOficial) {
        for (Asignacion a : cache) {
            if (a.isActivo() && a.getIdOficial().equalsIgnoreCase(idOficial)) {
                return a;
            }
        }
        return null;
    }
}
