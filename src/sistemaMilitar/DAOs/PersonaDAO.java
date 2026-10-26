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
import sistemaMilitar.models.Oficial;
import sistemaMilitar.models.Persona;
import sistemaMilitar.models.Soldado;

public class PersonaDAO implements ICrud<Persona, String> {

    private final String archivoPath = "personas.txt";
    private final List<Persona> cache = new ArrayList<>();

    public PersonaDAO() {
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
                if (partes.length >= 6) {
                    String tipo = partes[0].trim();
                    String id = partes[1].trim();
                    String dni = partes[2].trim();
                    String nombre = partes[3].trim();
                    String apellido = partes[4].trim();
                    boolean activo = Boolean.parseBoolean(partes[5].trim());

                    Persona p;
                    if ("OFICIAL".equalsIgnoreCase(tipo)) {
                        p = new Oficial(id, dni, nombre, apellido, activo);
                    } else {
                        p = new Soldado(id, dni, nombre, apellido, activo);
                    }
                    cache.add(p);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de personas", e);
        }
    }

    private void guardarEnArchivo() throws IOException {
        File archivo = new File(archivoPath);
        File temp = new File(archivoPath + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
            for (Persona p : cache) {
                String tipo = (p instanceof Oficial) ? "OFICIAL" : "SOLDADO";
                writer.write(tipo + "," + p.getId() + "," + p.getDni() + "," + p.getNombre() + "," + p.getApellido() + "," + p.isActivo());
                writer.newLine();
            }
        } catch (IOException e) {
            if (temp.exists()) {
                temp.delete();
            }
            throw new PersistenciaException("Error al escribir el archivo de personas", e);
        }

        if (archivo.exists() && !archivo.delete()) {
            throw new IOException("No se pudo reemplazar el archivo " + archivoPath);
        }
        if (!temp.renameTo(archivo)) {
            throw new IOException("No se pudo renombrar el archivo temporal de personas");
        }
    }

    @Override
    public void agregar(Persona entidad) throws IOException {
        if (buscarPorId(entidad.getId()) != null) {
            throw new EntidadDuplicadaException("Ya existe una persona con ID: " + entidad.getId());
        }
        if (buscarPorDni(entidad.getDni()) != null) {
            throw new EntidadDuplicadaException("Ya existe una persona con DNI: " + entidad.getDni());
        }
        cache.add(entidad);
        guardarEnArchivo();
    }

    @Override
    public List<Persona> listar() {
        return new ArrayList<>(cache);
    }

    @Override
    public Persona buscarPorId(String id) {
        for (Persona p : cache) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public Persona buscarPorDni(String dni) {
        for (Persona p : cache) {
            if (p.getDni().equalsIgnoreCase(dni)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Persona entidad) throws IOException {
        int index = -1;
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equalsIgnoreCase(entidad.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntidadNoEncontradaException("No se encontró la persona con ID: " + entidad.getId());
        }

        cache.set(index, entidad);
        guardarEnArchivo();
    }

    @Override
    public void eliminar(String id) throws IOException {
        Persona p = buscarPorId(id);
        if (p == null) {
            throw new EntidadNoEncontradaException("No se encontró la persona con ID: " + id);
        }
        p.setActivo(false);
        guardarEnArchivo();
    }

    @Override
    public void reactivar(String id) throws IOException {
        Persona p = buscarPorId(id);
        if (p == null) {
            throw new EntidadNoEncontradaException("No se encontró la persona con ID: " + id);
        }
        p.setActivo(true);
        guardarEnArchivo();
    }

    public List<String> obtenerTodosLosIds() {
        List<String> ids = new ArrayList<>();
        for (Persona p : cache) {
            ids.add(p.getId());
        }
        return ids;
    }
}
