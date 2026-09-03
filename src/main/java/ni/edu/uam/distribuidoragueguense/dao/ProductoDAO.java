package ni.edu.uam.distribuidoragueguense.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.distribuidoragueguense.interfaces.Crud;
import ni.edu.uam.distribuidoragueguense.model.Producto;

import java.util.List;
import java.util.Optional;

public class ProductoDAO implements Crud<Producto, String> {

    private final ObservableList<Producto> listaProductos;

    public ProductoDAO() {
        this.listaProductos = FXCollections.observableArrayList();
    }

    public ObservableList<Producto> getListaObservable() {

        return listaProductos;
    }

    @Override
    public void agregar(Producto producto) {

        listaProductos.add(producto);
    }

    @Override
    public List<Producto> obtenerRegistros() {

        return listaProductos;
    }

    @Override
    public Optional<Producto> buscarPorId(String codigo) {
        return listaProductos.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public boolean actualizar(Producto productoEditado) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getCodigo().equalsIgnoreCase(productoEditado.getCodigo())) {
                listaProductos.set(i, productoEditado);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String codigo) {
        return listaProductos.removeIf(p -> p.getCodigo().equalsIgnoreCase(codigo));
    }


}