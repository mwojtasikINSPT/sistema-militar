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
import sistemaMilitar.models.Cuartel;

public class CuartelDAO implements ICrud<Cuartel, String> {

    private final String archivoPath = "cuarteles.txt";
    private final List<Cuartel> cache = new ArrayList<>();

    public CuartelDAO() {
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
                if (partes.length >= 3) {
                    String codigo = partes[0].trim();
                    int capacidad = Integer.parseInt(partes[1].trim());
                    boolean activo = Boolean.parseBoolean(partes[2].trim());
                    cache.add(new Cuartel(codigo, capacidad, activo));
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de cuarteles", e);
        }
    }

    private void guardarEnArchivo() throws IOException {
        File archivo = new File(archivoPath);
        File temp = new File(archivoPath + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
            for (Cuartel c : cache) {
                writer.write(c.getCodigo() + "," + c.getCapacidad() + "," + c.isActivo());
                writer.newLine();
            }
        } catch (IOException e) {
            if (temp.exists()) {
                temp.delete();
            }
            throw new PersistenciaException("Error al escribir el archivo de cuarteles", e);
        }

        if (archivo.exists() && !archivo.delete()) {
            throw new IOException("No se pudo reemplazar el archivo " + archivoPath);
        }
        if (!temp.renameTo(archivo)) {
            throw new IOException("No se pudo renombrar el archivo temporal de cuarteles");
        }
    }

    @Override
    public void agregar(Cuartel entidad) throws IOException {
        if (buscarPorId(entidad.getCodigo()) != null) {
            throw new EntidadDuplicadaException("Ya existe un cuartel con código: " + entidad.getCodigo());
        }
        cache.add(entidad);
        guardarEnArchivo();
    }

    @Override
    public List<Cuartel> listar() {
        return new ArrayList<>(cache);
    }

    @Override
    public Cuartel buscarPorId(String id) {
        for (Cuartel c : cache) {
            if (c.getCodigo().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Cuartel entidad) throws IOException {
        int index = -1;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getCodigo().equalsIgnoreCase(entidad.getCodigo())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntidadNoEncontradaException("No se encontró el cuartel con código: " + entidad.getCodigo());
        }

        cache.set(index, entidad);
        guardarEnArchivo();
    }

    @Override
    public void eliminar(String id) throws IOException {
        Cuartel c = buscarPorId(id);
        if (c == null) {
            throw new EntidadNoEncontradaException("No se encontró el cuartel con código: " + id);
        }
        c.setActivo(false);
        guardarEnArchivo();
    }

    @Override
    public void reactivar(String id) throws IOException {
        Cuartel c = buscarPorId(id);
        if (c == null) {
            throw new EntidadNoEncontradaException("No se encontró el cuartel con código: " + id);
        }
        c.setActivo(true);
        guardarEnArchivo();
    }

    public List<String> obtenerTodosLosCodigos() {
        List<String> codigos = new ArrayList<>();
        for (Cuartel c : cache) {
            codigos.add(c.getCodigo());
        }
        return codigos;
    }
}
