package esfe.persistencia;

import esfe.dominio.Movimiento;
import org.junit.jupiter.api.*;
import esfe.dominio.Categoria;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovimientoDAOTest {

    static int idMovimiento;
    static int idCategoria;

    @BeforeAll
    static void setupCategoria() {
        Categoria categoria = new Categoria(0, "CategoriaTestMovimiento");
        CategoriaDAO.agregarCategoria(categoria);

        // Obtener ID
        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        for (Categoria c : categorias) {
            if (c.getNombre().equals("CategoriaTestMovimiento")) {
                idCategoria = c.getIdCategoria();
            }
        }
    }

    @Test
    @Order(1)
    void testAgregarMovimiento() {
        Movimiento mov = new Movimiento("Ingreso", "TestIngreso", 100.0, "2025-06-01", idCategoria);
        MovimientoDAO.agregarMovimiento(mov);
        List<Movimiento> lista = MovimientoDAO.obtenerMovimientos();

        boolean encontrado = false;
        for (Movimiento m : lista) {
            if (m.descripcion.equals("TestIngreso")) {
                idMovimiento = m.id;
                encontrado = true;
                break;
            }
        }

        assertTrue(encontrado);
        assertTrue(idMovimiento > 0);
    }

    @Test
    @Order(2)
    void testActualizarMovimiento() {
        Movimiento actualizado = new Movimiento("Ingreso", "Modificado", 150.0, "2025-06-01", idCategoria);
        actualizado.id = idMovimiento;

        MovimientoDAO.actualizarMovimiento(actualizado);
        List<Movimiento> lista = MovimientoDAO.obtenerMovimientos();

        boolean modificado = false;
        for (Movimiento m : lista) {
            if (m.id == idMovimiento && m.descripcion.equals("Modificado")) {
                modificado = true;
                break;
            }
        }

        assertTrue(modificado);
    }

    @Test
    @Order(3)
    void testFiltrarPorCategoria() {
        List<Movimiento> filtrados = MovimientoDAO.filtrarPorCategoria("CategoriaTestMovimiento");
        assertFalse(filtrados.isEmpty());
    }

    @Test
    @Order(4)
    void testFiltrarPorFecha() {
        Date desde = Date.valueOf("2025-06-01");
        Date hasta = Date.valueOf("2025-06-30");

        List<Movimiento> filtrados = MovimientoDAO.filtrarPorFecha(desde, hasta);
        assertFalse(filtrados.isEmpty());
    }

    @Test
    @Order(5)
    void testEliminarMovimiento() {
        MovimientoDAO.eliminarMovimiento(idMovimiento);
        List<Movimiento> lista = MovimientoDAO.obtenerMovimientos();

        boolean sigueExistiendo = lista.stream().anyMatch(m -> m.id == idMovimiento);
        assertFalse(sigueExistiendo);
    }

    @AfterAll
    static void limpiarCategoria() {
        CategoriaDAO.eliminarCategoria(idCategoria);
    }
}
