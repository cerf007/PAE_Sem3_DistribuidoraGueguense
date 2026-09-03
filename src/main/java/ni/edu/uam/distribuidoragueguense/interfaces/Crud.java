package ni.edu.uam.distribuidoragueguense.interfaces;

import java.util.List;
import java.util.Optional;

public interface Crud <T, ID> {
    void agregar(T entidad);

    List<T> obtenerRegistros();
    Optional<T> buscarPorId(ID id);

    boolean actualizar(T entidad);

    boolean eliminar(ID id);
}
