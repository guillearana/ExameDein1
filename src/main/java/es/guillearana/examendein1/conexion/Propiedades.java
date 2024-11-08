package es.guillearana.examendein1.conexion;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Clase abstracta para gestionar la carga y acceso a las propiedades de configuración.
 *
 * Esta clase se encarga de cargar las propiedades desde un archivo de configuración
 * (configuration.properties) al iniciar la clase, y proporciona un método estático
 * para acceder a los valores de las propiedades de configuración por clave.
 */
public abstract class Propiedades {

    /** Objeto que almacena las propiedades cargadas desde el archivo de configuración. */
    private static Properties props = new Properties();

    // Bloque estático para cargar la configuración desde un archivo de propiedades al iniciar la clase
    static {
        try (FileInputStream input = new FileInputStream("configuration.properties")) {
            // Cargamos las propiedades desde el archivo
            props.load(input);
        } catch (Exception e) {
            // Imprime la traza del error si ocurre una excepción durante la carga
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el valor asociado a una clave específica desde el archivo de propiedades.
     *
     * @param clave la clave de la propiedad que se desea obtener
     * @return el valor asociado a la clave proporcionada
     * @throws RuntimeException si la clave no existe en el archivo de propiedades
     */
    public static String getValor(String clave) {
        // Obtenemos el valor correspondiente a la clave proporcionada
        String valor = props.getProperty(clave);

        // Si la clave existe y tiene un valor asociado, lo retornamos
        if (valor != null) {
            return valor;
        }

        // En caso de que la clave no exista, lanzamos una excepción RuntimeException
        throw new RuntimeException("La clave solicitada en configuration.properties no está disponible");
    }
}
