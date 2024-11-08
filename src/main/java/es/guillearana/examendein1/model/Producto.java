package es.guillearana.examendein1.model;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private boolean disponible;

    public Producto(String codigo, String nombre, double precio, boolean disponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}

