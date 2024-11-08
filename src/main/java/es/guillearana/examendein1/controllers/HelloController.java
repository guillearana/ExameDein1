package es.guillearana.examendein1.controllers;

import es.guillearana.examendein1.model.Producto;
import es.guillearana.examendein1.dao.ProductoDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Controlador para la interfaz gráfica que gestiona productos.
 * Permite crear, actualizar y eliminar productos, así como seleccionar una imagen para cada uno.
 */
public class HelloController {

    @FXML
    private TextField codigoField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField precioField;

    @FXML
    private CheckBox disponibleCheckBox;

    @FXML
    private Button crearButton;

    @FXML
    private Button actualizarButton;

    @FXML
    private Button limpiarButton;

    @FXML
    private TableView<Producto> table;

    @FXML
    private TableColumn<Producto, String> codigoColumn;

    @FXML
    private TableColumn<Producto, String> nombreColumn;

    @FXML
    private TableColumn<Producto, Double> precioColumn;

    @FXML
    private TableColumn<Producto, Boolean> disponibleColumn;

    @FXML
    private Button seleccionarImagenButton;

    @FXML
    private ImageView imagenView;

    private ObservableList<Producto> productos = FXCollections.observableArrayList();
    private ProductoDao productoDao;
    private FileChooser fileChooser = new FileChooser();

    /**
     * Inicializa el controlador configurando las columnas de la tabla,
     * el menú contextual y cargando los productos de la base de datos.
     *
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @FXML
    public void initialize() throws SQLException {
        // Configurar columnas de la tabla
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // Asociar la lista de productos con la tabla
        table.setItems(productos);

        // Deshabilitar el botón de actualizar al inicio
        actualizarButton.setDisable(true);

        // Filtro de archivo para imágenes
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.jpg", "*.png"));

        // Cargar productos de la base de datos
        cargarProductos();

        // Crear y asignar el menú contextual
        ContextMenu contextMenu = new ContextMenu();
        MenuItem eliminarItem = new MenuItem("Eliminar");
        eliminarItem.setOnAction(event -> onEliminarMenuItemClicked());
        contextMenu.getItems().add(eliminarItem);
        table.setContextMenu(contextMenu);
    }

    /**
     * Carga los productos desde la base de datos.
     *
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    private void cargarProductos() throws SQLException {
        productoDao = new ProductoDao();
        try {
            productos.setAll(productoDao.obtenerTodos());
        } catch (SQLException e) {
            mostrarError("Error al cargar productos", "Hubo un error al cargar los productos.");
        }
    }

    /**
     * Muestra un diálogo para seleccionar una imagen y la carga en el ImageView.
     *
     * @param event el evento de selección de imagen
     */
    @FXML
    void onSeleccionarImagenButtonClicked(ActionEvent event) {
        var archivo = fileChooser.showOpenDialog(seleccionarImagenButton.getScene().getWindow());
        if (archivo != null) {
            Image image = new Image(archivo.toURI().toString());
            imagenView.setImage(image);
        }
    }

    /**
     * Crea un nuevo producto con los datos ingresados y lo guarda en la base de datos.
     *
     * @param event el evento de creación del producto
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @FXML
    void onCrearButtonClicked(ActionEvent event) throws SQLException {
        StringBuilder errores = new StringBuilder();

        String codigo = codigoField.getText();
        String nombre = nombreField.getText();
        String precioStr = precioField.getText();
        productoDao = new ProductoDao();

        if (codigo.length() != 5) {
            errores.append("El código debe tener exactamente 5 caracteres.\n");
        }
        if (nombre.isEmpty()) {
            errores.append("El nombre del producto es obligatorio.\n");
        }
        if (precioStr.isEmpty()) {
            errores.append("El precio del producto es obligatorio.\n");
        }

        double precio = -1;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            errores.append("El precio debe ser un número válido.\n");
        }

        if (errores.length() > 0) {
            mostrarError("Errores de validación", errores.toString());
            return;
        }

        Producto producto = new Producto(codigo, nombre, precio, disponibleCheckBox.isSelected());

        try {
            productoDao.crear(producto);
            productos.add(producto);
            table.refresh();
            limpiarCampos();
            mostrarExito("Producto creado exitosamente.");
        } catch (SQLException e) {
            mostrarError("Error en la base de datos", "Hubo un error al guardar el producto.");
        }
    }

    /**
     * Actualiza el producto seleccionado con los nuevos datos ingresados.
     *
     * @param event el evento de actualización del producto
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @FXML
    void onActualizarButtonClicked(ActionEvent event) throws SQLException {
        productoDao = new ProductoDao();
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            productoSeleccionado.setCodigo(codigoField.getText());
            productoSeleccionado.setNombre(nombreField.getText());

            try {
                productoSeleccionado.setPrecio(Double.parseDouble(precioField.getText()));
            } catch (NumberFormatException e) {
                System.out.println("Error: El precio debe ser un número.");
                return;
            }

            productoSeleccionado.setDisponible(disponibleCheckBox.isSelected());

            try {
                productoDao.actualizar(productoSeleccionado);
                table.refresh();
                mostrarExito("Producto actualizado exitosamente.");
            } catch (SQLException e) {
                mostrarError("Error al actualizar producto", "Hubo un error al actualizar el producto.");
            }

            actualizarButton.setDisable(true);
            limpiarCampos();
        }
    }

    /**
     * Elimina el producto seleccionado de la tabla y la base de datos.
     */
    private void onEliminarMenuItemClicked() {
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        productoDao = new ProductoDao();
        if (productoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar este producto?");
            alert.setContentText("El producto con código " + productoSeleccionado.getCodigo() + " será eliminado.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    productoDao.eliminar(productoSeleccionado.getCodigo());
                    productos.remove(productoSeleccionado);
                    table.refresh();
                    limpiarCampos();
                    mostrarExito("Producto eliminado correctamente.");
                    actualizarButton.setDisable(true);
                    crearButton.setDisable(false);
                    cargarProductos();
                } catch (SQLException e) {
                    mostrarError("Error en la base de datos", "Hubo un error al eliminar el producto.");
                    e.printStackTrace();
                }
            }
        } else {
            mostrarError("No hay producto seleccionado", "Por favor, selecciona un producto para eliminar.");
        }
    }

    /**
     * Limpia los campos del formulario y restablece el ImageView.
     */
    @FXML
    void onLimpiarButtonClicked(ActionEvent event) {
        limpiarCampos();
    }

    /**
     * Llama cuando se hace clic en la tabla para cargar los datos del producto seleccionado en los campos.
     */
    @FXML
    void onTableClicked() {
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            codigoField.setText(productoSeleccionado.getCodigo());
            nombreField.setText(productoSeleccionado.getNombre());
            precioField.setText(String.valueOf(productoSeleccionado.getPrecio()));
            disponibleCheckBox.setSelected(productoSeleccionado.isDisponible());

            codigoField.setDisable(true);
            actualizarButton.setDisable(false);
            crearButton.setDisable(true);
        }
    }

    /**
     * Limpia todos los campos de entrada del formulario.
     */
    private void limpiarCampos() {
        codigoField.clear();
        nombreField.clear();
        precioField.clear();
        disponibleCheckBox.setSelected(false);
        imagenView.setImage(null);
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     *
     * @param titulo el título del mensaje
     * @param mensaje el contenido del mensaje
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de éxito en un cuadro de diálogo.
     *
     * @param mensaje el contenido del mensaje
     */
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
