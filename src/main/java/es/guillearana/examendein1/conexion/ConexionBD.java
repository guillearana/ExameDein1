package es.guillearana.examendein1.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * Clase que gestiona la conexión a la base de datos.
 *
 * Esta clase se encarga de establecer una conexión con la base de datos utilizando
 * los valores de configuración (como la URL, el usuario y la contraseña) almacenados
 * en el archivo de propiedades. La conexión se configura para que realice las operaciones
 * de manera automática y se cierra adecuadamente cuando ya no sea necesaria.
 */
public class ConexionBD {

	/** Objeto que representa la conexión a la base de datos. */
	private Connection conexion;

	/**
	 * Constructor que establece la conexión a la base de datos.
	 * Utiliza los valores de configuración obtenidos desde el archivo de propiedades.
	 * La conexión también se configura con el ajuste de zona horaria predeterminado.
	 *
	 * @throws SQLException si ocurre un error al establecer la conexión con la base de datos
	 */
	public ConexionBD() throws SQLException {
		// Obtenemos la URL de conexión, el usuario y la contraseña desde las propiedades
		String url = Propiedades.getValor("url") + "?serverTimezone=" + TimeZone.getDefault().getID();
		String user = Propiedades.getValor("user");
		String password = Propiedades.getValor("password");

		// Creamos la conexión a la base de datos
		conexion = DriverManager.getConnection(url, user, password);

		// Configuramos la conexión para que realice las operaciones de manera automática
		conexion.setAutoCommit(true);
	}

	/**
	 * Obtiene la conexión a la base de datos.
	 *
	 * @return la conexión a la base de datos
	 */
	public Connection getConexion() {
		return conexion;
	}

	/**
	 * Cierra la conexión con la base de datos.
	 *
	 * Este método comprueba si la conexión está activa antes de intentar cerrarla.
	 * Si la conexión se cierra exitosamente, se imprime un mensaje indicando que
	 * la conexión ha sido cerrada.
	 *
	 * @throws SQLException Si hay un error al cerrar la conexión,
	 *                     como si la conexión ya se encuentra cerrada.
	 */
	public boolean isConnectionClosed(Connection conn) {
		try {
			return conn == null || conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * Verifica si la conexión está abierta.
	 *
	 * @return true si la conexión está abierta, false en caso contrario.
	 * @throws SQLException Si hay un error al verificar el estado de la conexión.
	 */
	public boolean isConnected() throws SQLException {
		return conexion != null && !conexion.isClosed();
	}
}
