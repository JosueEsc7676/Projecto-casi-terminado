package esfe.persistencia;

import esfe.dominio.Categoria;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoriaDAOTest {

    static int idGenerado;

    @Test
    @Order(1)
    void testAgregarCategoria() {
        Categoria categoria = new Categoria(0, "TestCategoria");
        boolean resultado = CategoriaDAO.agregarCategoria(categoria);
        assertTrue(resultado);
    }

    @Test
    @Order(2)
    void testObtenerCategorias() {
        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        assertNotNull(categorias);
        assertFalse(categorias.isEmpty());

        // Guardamos ID de la categoría "TestCategoria" para los siguientes tests
        for (Categoria c : categorias) {
            if (c.getNombre().equals("TestCategoria")) {
                idGenerado = c.getIdCategoria();
            }
        }

        assertTrue(idGenerado > 0);
    }

    @Test
    @Order(3)
    void testActualizarCategoria() {
        Categoria categoria = new Categoria(idGenerado, "CategoriaModificada");
        boolean actualizado = CategoriaDAO.actualizarCategoria(categoria);
        assertTrue(actualizado);
    }

    @Test
    @Order(4)
    void testEliminarCategoria() {
        boolean eliminado = CategoriaDAO.eliminarCategoria(idGenerado);
        assertTrue(eliminado);
    }
}
