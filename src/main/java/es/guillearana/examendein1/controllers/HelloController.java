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
 * Controlador principal de la interfaz de usuario de la aplicación.
 *
 * Esta clase gestiona los eventos de la interfaz de usuario y las interacciones
 * con los productos. Permite crear, actualizar, eliminar y visualizar productos
 * en una tabla. Además, se encarga de manejar la carga de imágenes para los productos.
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
     * Método que inicializa la vista, configura las columnas de la tabla y carga los productos
     * desde la base de datos.
     *
     * @throws SQLException si ocurre un error al cargar los productos desde la base de datos
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
        eliminarItem.setOnAction(event -> onEliminarMenuItemClicked()); // Acción al hacer clic en Eliminar
        contextMenu.getItems().add(eliminarItem);
        table.setContextMenu(contextMenu);
    }

    /**
     * Carga los productos desde la base de datos y los muestra en la tabla.
     *
     * @throws SQLException si ocurre un error al cargar los productos desde la base de datos
     */
    private void cargarProductos() throws SQLException {
        // Crear el DAO
        productoDao = new ProductoDao(); // Inicializamos el DAO
        try {
            productos.setAll(productoDao.obtenerTodos());
        } catch (SQLException e) {
            mostrarError("Error al cargar productos", "Hubo un error al cargar los productos.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace clic en el botón para seleccionar una imagen.
     * Muestra un selector de archivos y establece la imagen seleccionada en el ImageView.
     *
     * @param event el evento de clic en el botón
     */
    @FXML
    void onSeleccionarImagenButtonClicked(ActionEvent event) {
        // Mostrar el FileChooser para seleccionar una imagen
        var archivo = fileChooser.showOpenDialog(seleccionarImagenButton.getScene().getWindow());
        if (archivo != null) {
            // Mostrar la imagen seleccionada en el ImageView
            Image image = new Image(archivo.toURI().toString());
            imagenView.setImage(image);  // Establecer la imagen en el ImageView
        }
    }

    /**
     * Método que se ejecuta cuando se hace clic en el botón de crear producto.
     * Valida los datos y guarda el nuevo producto en la base de datos.
     *
     * @param event el evento de clic en el botón
     * @throws SQLException si ocurre un error al guardar el producto en la base de datos
     */
    @FXML
    void onCrearButtonClicked(ActionEvent event) throws SQLException {
        // Validación de datos
        StringBuilder errores = new StringBuilder();

        String codigo = codigoField.getText();
        String nombre = nombreField.getText();
        String precioStr = precioField.getText();
        // Crear el DAO
        productoDao = new ProductoDao(); // Inicializamos el DAO

        // Verificar que el código tenga exactamente 5 caracteres
        if (codigo.length() != 5) {
            errores.append("El código debe tener exactamente 5 caracteres.\n");
        }

        // Verificar que los campos nombre y precio no estén vacíos
        if (nombre.isEmpty()) {
            errores.append("El nombre del producto es obligatorio.\n");
        }
        if (precioStr.isEmpty()) {
            errores.append("El precio del producto es obligatorio.\n");
        }

        double precio = -1;
        try {
            // Verificar que el precio sea un número decimal válido
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            errores.append("El precio debe ser un número válido.\n");
        }

        if (errores.length() > 0) {
            // Mostrar los errores al usuario
            mostrarError("Errores de validación", errores.toString());
            return;
        }

        // Crear un nuevo producto
        Producto producto = new Producto(codigo, nombre, precio, disponibleCheckBox.isSelected());

        // Intentar guardar el producto en la base de datos
        try {
            productoDao.crear(producto);  // Guardar el producto en la base de datos

            // Agregar producto a la lista y refrescar la tabla
            productos.add(producto);
            table.refresh();

            // Limpiar el formulario y mostrar éxito
            limpiarCampos();
            mostrarExito("Producto creado exitosamente.");
        } catch (SQLException e) {
            // Mostrar error si ocurre un fallo en la base de datos
            mostrarError("Error en la base de datos", "Hubo un error al guardar el producto.");
        }
    }

    /**
     * Método que se ejecuta cuando se hace clic en el botón de actualizar producto.
     * Actualiza el producto seleccionado en la base de datos.
     *
     * @param event el evento de clic en el botón
     * @throws SQLException si ocurre un error al actualizar el producto en la base de datos
     */
    @FXML
    void onActualizarButtonClicked(ActionEvent event) throws SQLException {
        // Obtener el producto seleccionado
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            // Actualizar los valores del producto seleccionado
            productoSeleccionado.setCodigo(codigoField.getText());
            productoSeleccionado.setNombre(nombreField.getText());

            try {
                productoSeleccionado.setPrecio(Double.parseDouble(precioField.getText()));
            } catch (NumberFormatException e) {
                System.out.println("Error: El precio debe ser un número.");
                return;
            }

            productoSeleccionado.setDisponible(disponibleCheckBox.isSelected());

            // Verificar si se seleccionó una nueva imagen
            if (imagenView.getImage() != null) {
                // Asignar la imagen seleccionada al producto (deberás implementar el manejo de la imagen)
                // productoSeleccionado.setImagen(imagenView.getImage());
            }

            // Intentar actualizar el producto en la base de datos
            try {
                productoDao.actualizar(productoSeleccionado);
                table.refresh();
                mostrarExito("Producto actualizado exitosamente.");
            } catch (SQLException e) {
                mostrarError("Error al actualizar producto", "Hubo un error al actualizar el producto.");
            }

            // Deshabilitar el botón de actualizar y limpiar los campos
            actualizarButton.setDisable(true);
            limpiarCampos();
            // Habilitar "Crear" y restablecer "Código"
            crearButton.setDisable(false);
            codigoField.setDisable(false);
        }
    }

    /**
     * Método que se ejecuta cuando se hace clic en el ítem de eliminar en el menú contextual.
     * Elimina el producto seleccionado de la base de datos y de la tabla.
     */
    private void onEliminarMenuItemClicked(){
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        // Crear el DAO
        productoDao = new ProductoDao(); // Inicializamos el DAO
        if (productoSeleccionado != null) {
            // Crear la alerta de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar el producto?");
            alert.setContentText("Esta acción no puede deshacerse.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Intentar eliminar el producto de la base de datos
                    productoDao.eliminar(productoSeleccionado.getCodigo());

                    // Eliminar producto de la lista y refrescar la tabla
                    productos.remove(productoSeleccionado);
                    table.refresh();

                    // Mostrar mensaje de éxito
                    mostrarExito("Producto eliminado exitosamente.");
                } catch (SQLException e) {
                    // Mostrar mensaje de error si no se puede eliminar
                    mostrarError("Error al eliminar producto", "Hubo un error al eliminar el producto.");
                }
            }
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        codigoField.clear();
        nombreField.clear();
        precioField.clear();
        disponibleCheckBox.setSelected(false);
        imagenView.setImage(null);
    }

    /**
     * Muestra una alerta de error con un título y un mensaje.
     *
     * @param titulo el título de la alerta
     * @param mensaje el mensaje a mostrar en la alerta
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de éxito con un mensaje.
     *
     * @param mensaje el mensaje a mostrar en la alerta
     */
    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación exitosa");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
