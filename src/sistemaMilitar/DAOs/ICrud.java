package sistemaMilitar.DAOs;

import java.io.IOException;
import java.util.List;

public interface ICrud<T, ID> {

    void agregar(T entidad) throws IOException;

    List<T> listar();

    T buscarPorId(ID id);

    void actualizar(T entidad) throws IOException;

    void eliminar(ID id) throws IOException;

    void reactivar(ID id) throws IOException;
}
