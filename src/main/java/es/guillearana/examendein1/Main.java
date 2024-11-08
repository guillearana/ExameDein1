package es.guillearana.examendein1;

import es.guillearana.examendein1.conexion.Propiedades;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase principal de la aplicación JavaFX que carga la interfaz de usuario y configura
 * la escena de la ventana principal con un fondo y estilos definidos.
 */
public class Main extends Application {

    /**
     * Método que se ejecuta al iniciar la aplicación, configurando el idioma,
     * cargando la interfaz y mostrando la ventana principal.
     *
     * @param primaryStage La ventana principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la interfaz de usuario desde un archivo FXML
            //GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("/es/guillearana/proyecto1/Listado.fxml"));

            /*// Crear un StackPane como contenedor principal
            StackPane stackPane = new StackPane();

            // Crear una imagen de fondo y un ImageView
            Image fondo = new Image(getClass().getResourceAsStream("/es/guillearana/proyecto1/imagenes/fondo.jpg"));
            ImageView imageView = new ImageView(fondo);
            // Ajustar la imagen al tamaño del StackPane
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
            imageView.setPreserveRatio(true);

            // Añadir la imagen como fondo al StackPane
            stackPane.getChildren().add(imageView);  // Añadir imagen como primer hijo (fondo)

            // Añadir el GridPane (contenido) encima del fondo
            stackPane.getChildren().add(root);  // Añadir GridPane como segundo hijo*/

            // Crear la escena y agregar hojas de estilo
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 475, 627);
            //scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
            primaryStage.setMaxHeight(755);
            primaryStage.setMaxWidth(1150);
            primaryStage.setMinHeight(500);
            primaryStage.setMinWidth(400);

            // Establecer el título de la ventana.
            primaryStage.setTitle("Fruta");

            // Establecer la escena en la ventana principal y mostrarla
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            // Manejar excepciones imprimiendo la traza de errores
            e.printStackTrace();
        }
    }

    /**
     * Método principal que inicia la aplicación JavaFX.
     *
     * @param args Los argumentos de línea de comandos pasados a la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
