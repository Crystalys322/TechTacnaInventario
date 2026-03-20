package techtacnainventarioentradajhony2026i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Prototipo académico de consola para gestión de inventario - TechTacna.
 */
public class TechTacnaInventarioEntradaJhony2026I {

    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        // Datos de ejemplo
        inventario.agregarProducto(new Producto("P001", "Mouse Gamer", 75.50, 12, 5));
        inventario.agregarProducto(new Producto("P002", "Teclado Mecánico", 210.00, 4, 6));
        inventario.agregarProducto(new Producto("P003", "Monitor 24\"", 680.00, 3, 2));
        inventario.agregarProducto(new Producto("P004", "SSD 1TB", 320.00, 1, 4));
        inventario.agregarProducto(new Producto("P005", "Audífonos", 95.00, 9, 5));

        // Operaciones de negocio de ejemplo
        inventario.registrarIngresoStock("P004", 7);   // ingreso de stock
        inventario.registrarSalidaStock("P002", 2);    // venta/salida
        inventario.actualizarStockProducto("P003", 2); // ajuste manual

        inventario.mostrarReporteGeneral();
        inventario.mostrarReporteStockCritico();

        // Extra opcional: producto de mayor valor en inventario
        Producto mayorValor = inventario.obtenerProductoMayorValor();
        if (mayorValor != null) {
            System.out.println("\nProducto con mayor valor en inventario:");
            System.out.println("  " + mayorValor.resumenCorto());
        }

        System.out.println("\n=== RESUMEN FINAL ===");
        System.out.println("Total de productos registrados: " + inventario.getCantidadProductos());
        System.out.printf("Valor total del inventario: S/ %.2f%n", inventario.calcularValorTotalInventario());
        System.out.println("Cantidad de productos con stock crítico: " + inventario.obtenerProductosStockCritico().size());
    }
}

/**
 * Entidad de dominio: cada instancia representa un producto.
 */
class Producto {

    private final String codigo;
    private final String nombre;
    private final double precioUnitario;
    private int stockActual;
    private final int stockMinimo;

    public Producto(String codigo, String nombre, double precioUnitario, int stockActual, int stockMinimo) {
        validarTexto(codigo, "código");
        validarTexto(nombre, "nombre");
        validarNoNegativo(precioUnitario, "precio unitario");
        validarNoNegativo(stockActual, "stock actual");
        validarNoNegativo(stockMinimo, "stock mínimo");

        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    // Cálculo de negocio: valor monetario de este producto en inventario.
    public double calcularValorInventario() {
        return precioUnitario * stockActual;
    }

    // Regla de negocio: stock crítico cuando stock actual <= stock mínimo.
    public boolean tieneStockCritico() {
        return stockActual <= stockMinimo;
    }

    public boolean actualizarStock(int nuevoStock) {
        if (nuevoStock < 0) {
            return false;
        }
        stockActual = nuevoStock;
        return true;
    }

    public boolean registrarIngreso(int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        stockActual += cantidad;
        return true;
    }

    public boolean registrarSalida(int cantidad) {
        if (cantidad <= 0 || cantidad > stockActual) {
            return false;
        }
        stockActual -= cantidad;
        return true;
    }

    public String resumenCorto() {
        return String.format("[%s] %s | Stock: %d | Valor: S/ %.2f", codigo, nombre, stockActual, calcularValorInventario());
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    private static void validarNoNegativo(double valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El " + campo + " no puede ser negativo.");
        }
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio.");
        }
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-18s | S/ %8.2f | %5d | %6d | S/ %9.2f",
                codigo, nombre, precioUnitario, stockActual, stockMinimo, calcularValorInventario());
    }
}

/**
 * Clase de servicio del dominio: administra colección de productos.
 */
class Inventario {

    private final List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public boolean agregarProducto(Producto producto) {
        if (producto == null || buscarProductoPorCodigo(producto.getCodigo()) != null) {
            return false;
        }
        productos.add(producto);
        return true;
    }

    public Producto buscarProductoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        String codigoNormalizado = codigo.trim().toUpperCase();
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigoNormalizado)) {
                return producto;
            }
        }
        return null;
    }

    public boolean actualizarStockProducto(String codigo, int nuevoStock) {
        Producto producto = buscarProductoPorCodigo(codigo);
        return producto != null && producto.actualizarStock(nuevoStock);
    }

    public boolean registrarIngresoStock(String codigo, int cantidad) {
        Producto producto = buscarProductoPorCodigo(codigo);
        return producto != null && producto.registrarIngreso(cantidad);
    }

    public boolean registrarSalidaStock(String codigo, int cantidad) {
        Producto producto = buscarProductoPorCodigo(codigo);
        return producto != null && producto.registrarSalida(cantidad);
    }

    public double calcularValorTotalInventario() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.calcularValorInventario();
        }
        return total;
    }

    public List<Producto> obtenerProductosStockCritico() {
        List<Producto> criticos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.tieneStockCritico()) {
                criticos.add(producto);
            }
        }
        return criticos;
    }

    public Producto obtenerProductoMayorValor() {
        if (productos.isEmpty()) {
            return null;
        }

        Producto mayor = productos.get(0);
        for (Producto producto : productos) {
            if (producto.calcularValorInventario() > mayor.calcularValorInventario()) {
                mayor = producto;
            }
        }
        return mayor;
    }

    public List<Producto> obtenerProductosOrdenadosPorNombre() {
        List<Producto> copia = new ArrayList<>(productos);
        copia.sort(Comparator.comparing(Producto::getNombre));
        return copia;
    }

    public int getCantidadProductos() {
        return productos.size();
    }

    public void mostrarReporteGeneral() {
        System.out.println("==============================================================");
        System.out.println("               REPORTE GENERAL DE INVENTARIO");
        System.out.println("==============================================================");

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.println("COD.  | NOMBRE             |  PRECIO U. | STOCK | MÍNIMO | VALOR TOTAL");
        System.out.println("----------------------------------------------------------------------");
        for (Producto producto : obtenerProductosOrdenadosPorNombre()) {
            System.out.println(producto);
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("VALOR TOTAL INVENTARIO: S/ %.2f%n", calcularValorTotalInventario());
    }

    public void mostrarReporteStockCritico() {
        List<Producto> criticos = obtenerProductosStockCritico();

        System.out.println("\n=== ALERTAS DE STOCK CRÍTICO ===");
        if (criticos.isEmpty()) {
            System.out.println("✅ No hay productos en estado crítico.");
            return;
        }

        for (Producto producto : criticos) {
            System.out.printf("⚠️  %s (%s): stock %d, mínimo %d%n",
                    producto.getNombre(),
                    producto.getCodigo(),
                    producto.getStockActual(),
                    producto.getStockMinimo());
        }
    }

    @Override
    public String toString() {
        return String.format("Inventario{productos=%d, valorTotal=S/ %.2f}",
                getCantidadProductos(),
                calcularValorTotalInventario());
    }
}
