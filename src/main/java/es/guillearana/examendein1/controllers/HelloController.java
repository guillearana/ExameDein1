package es.guillearana.examendein1.controllers;

import es.guillearana.examendein1.model.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private FileChooser fileChooser = new FileChooser();

    @FXML
    public void initialize() {
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
    }

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

    @FXML
    void onCrearButtonClicked(ActionEvent event) {
        // Validación de datos
        StringBuilder errores = new StringBuilder();

        String codigo = codigoField.getText();
        String nombre = nombreField.getText();
        String precioStr = precioField.getText();

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

        // Intentar guardar el producto en la base de datos (aquí debes incluir tu lógica de base de datos)
        try {
            // Ejemplo de guardado en base de datos, aquí debes conectar tu lógica
            // guardarProductoEnBaseDeDatos(producto);

            // Agregar producto a la lista y refrescar la tabla
            productos.add(producto);
            table.refresh();

            // Limpiar el formulario y mostrar éxito
            limpiarCampos();
            mostrarExito("Producto creado exitosamente.");
        } catch (Exception e) {
            // Mostrar error si ocurre un fallo en la base de datos
            mostrarError("Error en la base de datos", "Hubo un error al guardar el producto.");
        }
    }

    @FXML
    void onActualizarButtonClicked(ActionEvent event) {
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

            // Refrescar la tabla
            table.refresh();

            // Deshabilitar el botón de actualizar y limpiar los campos
            actualizarButton.setDisable(true);
            limpiarCampos();
        }
    }

    @FXML
    void onLimpiarButtonClicked(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    void onTableClicked() {
        // Obtener el producto seleccionado
        Producto productoSeleccionado = table.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            // Llenar los campos con los datos del producto seleccionado
            codigoField.setText(productoSeleccionado.getCodigo());
            nombreField.setText(productoSeleccionado.getNombre());
            precioField.setText(String.valueOf(productoSeleccionado.getPrecio()));
            disponibleCheckBox.setSelected(productoSeleccionado.isDisponible());

            // Habilitar el botón de actualizar
            actualizarButton.setDisable(false);
        }
    }

    private void limpiarCampos() {
        codigoField.clear();
        nombreField.clear();
        precioField.clear();
        disponibleCheckBox.setSelected(false);
        imagenView.setImage(null);  // Limpiar la vista previa de la imagen
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
