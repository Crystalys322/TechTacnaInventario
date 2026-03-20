package techtacnainventarioentradajhony2026i;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototipo de consola para gestión básica de inventario - TechTacna.
 */
public class TechTacnaInventarioEntradaJhony2026I {

    public static void main(String[] args) {
        // 1) Crear inventario
        Inventario inventario = new Inventario();

        // 2) Registrar productos de ejemplo
        inventario.agregarProducto(new Producto("P001", "Mouse Gamer", 75.50, 12, 5));
        inventario.agregarProducto(new Producto("P002", "Teclado Mecánico", 210.00, 4, 6));
        inventario.agregarProducto(new Producto("P003", "Monitor 24\"", 680.00, 3, 2));
        inventario.agregarProducto(new Producto("P004", "SSD 1TB", 320.00, 1, 4));
        inventario.agregarProducto(new Producto("P005", "Audífonos", 95.00, 9, 5));

        // 3) Mostrar reporte completo
        inventario.mostrarReporte();

        // 4) Bloque breve para exponer en clase (resumen ejecutivo)
        System.out.println("\n=== RESUMEN PARA EXPOSICIÓN ===");
        System.out.println("Total de productos registrados: " + inventario.getCantidadProductos());
        System.out.printf("Valor total del inventario: S/ %.2f%n", inventario.calcularValorTotalInventario());
        System.out.println("Cantidad de productos en stock crítico: " + inventario.obtenerProductosCriticos().size());
    }
}

/**
 * Entidad de dominio: representa un producto del inventario.
 */
class Producto {

    private String codigo;
    private String nombre;
    private double precioUnitario;
    private int stockActual;
    private int stockMinimo;

    public Producto(String codigo, String nombre, double precioUnitario, int stockActual, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    // Método de negocio: valor en inventario por producto
    public double calcularValorInventario() {
        return precioUnitario * stockActual;
    }

    // Método de negocio: detección de stock crítico
    public boolean tieneStockCritico() {
        return stockActual <= stockMinimo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s | Precio: S/ %8.2f | Stock: %3d | Mínimo: %3d | Valor: S/ %9.2f",
                codigo, nombre, precioUnitario, stockActual, stockMinimo, calcularValorInventario());
    }
}

/**
 * Agregado de dominio: administra el conjunto de productos.
 */
class Inventario {

    private final List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            productos.add(producto);
        }
    }

    public double calcularValorTotalInventario() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.calcularValorInventario();
        }
        return total;
    }

    public List<Producto> obtenerProductosCriticos() {
        List<Producto> criticos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.tieneStockCritico()) {
                criticos.add(producto);
            }
        }
        return criticos;
    }

    public int getCantidadProductos() {
        return productos.size();
    }

    public void mostrarReporte() {
        System.out.println("==============================================");
        System.out.println("      REPORTE DE INVENTARIO - TECHTACNA");
        System.out.println("==============================================");

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }

        System.out.println("\nLista de productos:");
        for (Producto producto : productos) {
            System.out.println(producto);
        }

        System.out.printf("\nValor total del inventario: S/ %.2f%n", calcularValorTotalInventario());

        System.out.println("\nAlertas de stock crítico:");
        List<Producto> criticos = obtenerProductosCriticos();

        if (criticos.isEmpty()) {
            System.out.println("✅ No hay productos en estado crítico.");
        } else {
            for (Producto producto : criticos) {
                System.out.printf("⚠️  ALERTA: %s (%s) con stock %d (mínimo %d)%n",
                        producto.getNombre(),
                        producto.getCodigo(),
                        producto.getStockActual(),
                        producto.getStockMinimo());
            }
        }
    }
}
