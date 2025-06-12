package esfe.persistencia;

import esfe.dominio.Movimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
public class MovimientoDAO {

    public static void agregarMovimiento(Movimiento mov) {
        String sql = "INSERT INTO Movimiento (tipo, descripcion, monto, fecha, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mov.tipo);
            pstmt.setString(2, mov.descripcion);
            pstmt.setDouble(3, mov.monto);
            pstmt.setString(4, mov.fecha);
            pstmt.setInt(5, mov.id_categoria);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void eliminarMovimiento(int id) {
        String sql = "DELETE FROM Movimiento WHERE id = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Movimiento> obtenerMovimientos() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.tipo, m.descripcion, m.monto, m.fecha, m.id_categoria, c.nombre " +
                "FROM Movimiento m " +
                "LEFT JOIN categorias c ON m.id_categoria = c.id_categoria";

        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movimiento m = new Movimiento(
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha").toString(),
                        rs.getInt("id_categoria")
                );
                m.id = rs.getInt("id");
                m.nombreCategoria = rs.getString("nombre"); // ✅ Agregar nombre de categoría
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public static Map<String, Double> obtenerIngresosPorMes() {
        Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
        String sql = "SELECT FORMAT(fecha, 'yyyy-MM') as mes, SUM(monto) as total " +
                "FROM Movimiento WHERE tipo = 'Ingreso' " +
                "GROUP BY FORMAT(fecha, 'yyyy-MM') " +
                "ORDER BY FORMAT(fecha, 'yyyy-MM') ASC";

        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingresosPorMes.put(rs.getString("mes"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingresosPorMes;
    }

    public static Map<String, Double> obtenerGastosPorMes() {
        Map<String, Double> gastosPorMes = new LinkedHashMap<>();
        String sql = "SELECT FORMAT(fecha, 'yyyy-MM') as mes, SUM(monto) as total " +
                "FROM Movimiento WHERE tipo = 'Egreso' " +
                "GROUP BY FORMAT(fecha, 'yyyy-MM') " +
                "ORDER BY FORMAT(fecha, 'yyyy-MM') ASC";


        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String mes = rs.getString("mes");
                Double total = rs.getDouble("total");
                System.out.println("Mes: " + mes + " | Total: " + total); // ✅ Verifica los datos
                gastosPorMes.put(mes, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gastosPorMes;
    }


    public static List<Movimiento> filtrarPorCategoria(String categoria) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.tipo, m.descripcion, m.monto, m.fecha, m.id_categoria, c.nombre " +
                "FROM Movimiento m " +
                "LEFT JOIN categorias c ON m.id_categoria = c.id_categoria " +
                "WHERE c.nombre = ?";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoria);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movimiento m = new Movimiento(
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha").toString(),
                        rs.getInt("id_categoria")
                );
                m.id = rs.getInt("id");
                m.nombreCategoria = rs.getString("nombre"); // ✅ Agregar nombre de categoría
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public static List<String> obtenerCategoriasUnicas() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT nombre FROM Categoria ORDER BY nombre ASC";
        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    // Corrección al método actualizarMovimiento
    public static void actualizarMovimiento(Movimiento mov) {
        String sql = "UPDATE Movimiento SET tipo = ?, descripcion = ?, monto = ?, fecha = ?, id_categoria = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mov.tipo);
            pstmt.setString(2, mov.descripcion);
            pstmt.setDouble(3, mov.monto);
            pstmt.setString(4, mov.fecha);
            pstmt.setInt(5, mov.id_categoria);
            pstmt.setInt(6, mov.id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Movimiento> filtrarPorFecha(Date desde, Date hasta) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.tipo, m.descripcion, m.monto, m.fecha, m.id_categoria, c.nombre " +
                "FROM Movimiento m " +
                "LEFT JOIN categorias c ON m.id_categoria = c.id_categoria " +
                "WHERE m.fecha BETWEEN ? AND ?";

        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, desde);
            pstmt.setDate(2, hasta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movimiento m = new Movimiento(
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha").toString(),
                        rs.getInt("id_categoria")
                );
                m.id = rs.getInt("id");
                m.nombreCategoria = rs.getString("nombre"); // ✅ Agregar nombre de categoría
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public static double calcularTotalPorTipo(String tipo) {
        String sql = "SELECT SUM(monto) as total FROM Movimiento WHERE tipo = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
