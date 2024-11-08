package es.guillearana.examendein1.dao;

import es.guillearana.examendein1.conexion.ConexionBD;
import es.guillearana.examendein1.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar las operaciones de la tabla "productos" en la base de datos.
 * Contiene métodos para crear, obtener, actualizar y eliminar productos.
 */
public class ProductoDao {

    // Conexión a la base de datos, se obtiene desde la clase ConexionBD
    private ConexionBD conexionBD;

    /**
     * Constructor para inicializar la conexión a la base de datos utilizando la clase ConexionBD.
     *
     * @throws RuntimeException si ocurre un error al obtener la conexión a la base de datos
     */
    public ProductoDao(){
        try {
            this.conexionBD = new ConexionBD();  // Obtener la conexión desde ConexionBD
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param producto el producto a crear
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    public void crear(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (codigo, nombre, precio, disponible) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionBD.getConexion();  // Usamos conexionBD.getConexion() para obtener la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.isDisponible());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Error al crear el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return una lista con todos los productos
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = conexionBD.getConexion();  // Usamos conexionBD.getConexion() para obtener la conexión
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener todos los productos: " + e.getMessage(), e);
        }
        return productos;
    }

    /**
     * Actualiza un producto en la base de datos.
     *
     * @param producto el producto con los datos actualizados
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, disponible = ? WHERE codigo = ?";
        try (Connection conn = conexionBD.getConexion();  // Usamos conexionBD.getConexion() para obtener la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setBoolean(3, producto.isDisponible());
            stmt.setString(4, producto.getCodigo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un producto de la base de datos utilizando su código.
     *
     * @param codigo el código del producto a eliminar
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (Connection conn = conexionBD.getConexion();  // Usamos conexionBD.getConexion() para obtener la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }
}
