package ni.edu.uam.distribuidoragueguense.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.distribuidoragueguense.dao.ProductoDAO;
import ni.edu.uam.distribuidoragueguense.model.Producto;

import java.util.Optional;

public class ProductoController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExistencia;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colExistencia;

    @FXML private Label lblMensaje;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private boolean modoEdicion = false;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));

        tablaProductos.setItems(productoDAO.getListaObservable());

        lblMensaje.setText("Estado: Listo. Se cargaron " + productoDAO.obtenerRegistros().size() + " productos iniciales.");
    }


    @FXML
    private void nuevoProducto() {
        limpiarFormulario();
        lblMensaje.setText("Estado: Formulario listo para ingresar un nuevo producto.");
        txtCodigo.requestFocus();
    }

    @FXML
    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtCategoria.clear();
        txtPrecio.clear();
        txtExistencia.clear();

        txtCodigo.setDisable(false);
        modoEdicion = false;
        tablaProductos.getSelectionModel().clearSelection();
    }

    @FXML
    private void guardarProducto() {
        if (!validarCamposFormulario()) {
            return;
        }

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();
        double precio;
        int existencia;

        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            existencia = Integer.parseInt(txtExistencia.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de tipo de dato", "Precio y Existencia deben ser números válidos.");
            return;
        }

        Producto producto = new Producto(codigo, nombre, categoria, precio, existencia);

        if (modoEdicion) {
            productoDAO.actualizar(producto);
            lblMensaje.setText("Estado: Producto [" + codigo + "] actualizado correctamente.");
        } else {
            if (productoDAO.buscarPorId(codigo).isPresent()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código Duplicado", "Ya existe un producto registrado con el código: " + codigo);
                return;
            }
            productoDAO.agregar(producto);
            lblMensaje.setText("Estado: Producto [" + codigo + "] guardado exitosamente.");
        }

        limpiarFormulario();
    }

    @FXML
    private void cargarProductoParaEditar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un producto de la tabla para editar.");
            lblMensaje.setText("Estado: Operación cancelada. No se seleccionó ningún producto.");
            return;
        }

        txtCodigo.setText(seleccionado.getCodigo());
        txtNombre.setText(seleccionado.getNombre());
        txtCategoria.setText(seleccionado.getCategoria());
        txtPrecio.setText(String.valueOf(seleccionado.getPrecio()));
        txtExistencia.setText(String.valueOf(seleccionado.getExistencia()));

        txtCodigo.setDisable(true);
        modoEdicion = true;
        lblMensaje.setText("Estado: Editando producto [" + seleccionado.getCodigo() + "]");
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un producto de la tabla para eliminar.");
            lblMensaje.setText("Estado: Operación cancelada. No se seleccionó ningún producto.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el producto \"" + seleccionado.getNombre() + "\"?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            productoDAO.eliminar(seleccionado.getCodigo());
            limpiarFormulario();
            lblMensaje.setText("Estado: Producto [" + seleccionado.getCodigo() + "] eliminado.");
        }
    }

    @FXML
    private void verDetalleProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un producto para ver el detalle.");
            lblMensaje.setText("Estado: Operación cancelada. No hay un producto seleccionado.");
            return;
        }

        String detalle = String.format(
                "Código: %s\nNombre: %s\nCategoría: %s\nPrecio: C$ %.2f\nExistencia: %d unidades",
                seleccionado.getCodigo(),
                seleccionado.getNombre(),
                seleccionado.getCategoria(),
                seleccionado.getPrecio(),
                seleccionado.getExistencia()
        );

        mostrarAlerta(Alert.AlertType.INFORMATION, "Detalle del Producto", detalle);
        lblMensaje.setText("Estado: Mostrando detalle del producto [" + seleccionado.getCodigo() + "]");
    }

    @FXML
    private void mostrarAcercaDe() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Acerca de",
                "Distribuidora El Güegüense v1.0\n\n" +
                        "Aplicación desarrollada para la práctica de Menús, ToolBar y ContextMenu.\n" +
                        "Programación de Aplicaciones de Escritorio - UAM");
    }

    @FXML
    private void salirAplicacion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Salir de la aplicación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea salir?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            Platform.exit();
        }
    }


    private boolean validarCamposFormulario() {
        if (txtCodigo.getText().trim().isEmpty() ||
                txtNombre.getText().trim().isEmpty() ||
                txtCategoria.getText().trim().isEmpty() ||
                txtPrecio.getText().trim().isEmpty() ||
                txtExistencia.getText().trim().isEmpty()) {

            mostrarAlerta(Alert.AlertType.ERROR, "Campos Vacíos", "Por favor complete todos los campos del formulario.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}