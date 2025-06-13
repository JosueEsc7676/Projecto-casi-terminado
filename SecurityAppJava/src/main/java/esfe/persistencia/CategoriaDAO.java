package esfe.persistencia;

import esfe.dominio.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Metodo para obtener todas las categorías desde sql
    public static List<Categoria> obtenerCategorias() {
        List<Categoria> lista = new ArrayList<>(); // Lista que guarda las categorías que se obtienen.
        String sql = "SELECT id_categoria, nombre FROM categorias"; // Consulta sql.

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Conexión a la base de datos.
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)                        // Ejecuta la consulta y guarda resultados.
        ) {
            while (rs.next()) { // Recorre cada fila obtenida.
                Categoria c = new Categoria( // Crea un nuevo objeto Categoria con los datos de la fila.
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
                lista.add(c); // Agrega la categoría a la lista.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista; // Devuelve la lista completa de categorías.
    }

    // Metodo para actualizar el nombre de una categoría existente
    public static boolean actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id_categoria = ?"; // Consulta para actualizar.

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Conecta a la base de datos.
                PreparedStatement pstmt = conn.prepareStatement(sql)         // Prepara la consulta con parámetros.
        ) {
            pstmt.setString(1, categoria.getNombre());         // Primer parámetro: nuevo nombre.
            pstmt.setInt(2, categoria.getIdCategoria());       // Segundo parámetro: ID de la categoría a actualizar.
            int filas = pstmt.executeUpdate();                 // Ejecuta la actualización.
            return filas > 0; // Devuelve true si se actualizó al menos una fila.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // false si no se hizo ningún cambio o hubo error.
    }

    // Metodo para eliminar una categoría según su ID
    public static boolean eliminarCategoria(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?"; // Consulta para eliminar.

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Conecta a la base de datos.
                PreparedStatement pstmt = conn.prepareStatement(sql)         // Prepara la consulta con el ID.
        ) {
            pstmt.setInt(1, idCategoria); // Establece el ID de la categoría que se quiere eliminar.
            int filas = pstmt.executeUpdate(); // Ejecuta la eliminación.
            return filas > 0; // Devuelve true si se eliminó correctamente.
        } catch (SQLException e) {
            e.printStackTrace(); // Muestra errores si algo falla.
        }
        return false;
    }

    // Metodo para agregar una nueva categoría
    public static boolean agregarCategoria(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)"; // Consulta para insertar una nueva categoría.

        try (
                Connection conn = ConnectionManager.getInstance().connect(); // Conecta a la base de datos.
                PreparedStatement pstmt = conn.prepareStatement(sql)         // Prepara la consulta con parámetros.
        ) {
            pstmt.setString(1, categoria.getNombre()); // Establece el nombre de la nueva categoría.
            int filas = pstmt.executeUpdate(); // Ejecuta la inserción.
            return filas > 0; // Devuelve true si se insertó al menos una fila.
        } catch (SQLException e) {
            e.printStackTrace(); // Muestra errores si algo falla.
        }
        return false;
    }
}
