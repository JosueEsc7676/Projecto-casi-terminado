package esfe.persistencia;

import esfe.dominio.Movimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public static void agregarMovimiento(Movimiento mov) {
        String sql = "INSERT INTO Movimiento (tipo, descripcion, monto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mov.tipo);
            pstmt.setString(2, mov.descripcion);
            pstmt.setDouble(3, mov.monto);
            pstmt.setString(4, mov.fecha);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarMovimiento(Movimiento mov) {
        String sql = "UPDATE Movimiento SET tipo = ?, descripcion = ?, monto = ?, fecha = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mov.tipo);
            pstmt.setString(2, mov.descripcion);
            pstmt.setDouble(3, mov.monto);
            pstmt.setString(4, mov.fecha);
            pstmt.setInt(5, mov.id);
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
        String sql = "SELECT * FROM Movimiento";
        try (Connection conn = ConnectionManager.getInstance().connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movimiento m = new Movimiento(
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha").toString()
                );
                m.id = rs.getInt("id");
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Movimiento> obtenerMovimientosPorFecha(Date desde, Date hasta) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Movimiento WHERE fecha BETWEEN ? AND ?";
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
                        rs.getDate("fecha").toString()
                );
                m.id = rs.getInt("id");
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
