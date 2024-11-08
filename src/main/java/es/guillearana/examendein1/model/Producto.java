package es.guillearana.examendein1.model;

/**
 * Clase que representa un producto con atributos como código, nombre, precio y disponibilidad.
 * Proporciona los métodos getter y setter para manipular estos atributos.
 */
public class Producto {

    private String codigo;
    private String nombre;
    private double precio;
    private boolean disponible;

    /**
     * Constructor para inicializar un producto con sus atributos.
     *
     * @param codigo el código del producto
     * @param nombre el nombre del producto
     * @param precio el precio del producto
     * @param disponible el estado de disponibilidad del producto
     */
    public Producto(String codigo, String nombre, double precio, boolean disponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    /**
     * Obtiene el código del producto.
     *
     * @return el código del producto
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del producto.
     *
     * @param codigo el nuevo código del producto
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return el nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre el nuevo nombre del producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return el precio del producto
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio el nuevo precio del producto
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el estado de disponibilidad del producto.
     *
     * @return true si el producto está disponible, false si no lo está
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece el estado de disponibilidad del producto.
     *
     * @param disponible el nuevo estado de disponibilidad del producto
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
