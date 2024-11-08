package es.guillearana.examendein1.dao;

import es.guillearana.examendein1.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    // Datos de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/mi_base_de_datos";
    private static final String USER = "usuario";
    private static final String PASSWORD = "contraseña";

    // Conexión a la base de datos
    private Connection conexion;

    // Constructor para inicializar la conexión
    public ProductoDao() throws SQLException {
        this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Crear un nuevo producto
    public void crear(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (codigo, nombre, precio, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.isDisponible());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al crear el producto: " + e.getMessage(), e);
        }
    }

    // Obtener un producto por código
    public Producto obtenerPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getBoolean("disponible")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el producto por código: " + e.getMessage(), e);
        }
        return null;
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
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

    // Actualizar un producto
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, disponible = ?, imagen = ? WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setBoolean(3, producto.isDisponible());
            stmt.setString(5, producto.getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    // Eliminar un producto por código
    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}
