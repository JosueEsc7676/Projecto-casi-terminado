package esfe.presentacion;

import com.toedter.calendar.JDateChooser;
import esfe.dominio.Movimiento;
import esfe.persistencia.MovimientoDAO;
import esfe.persistencia.CategoriaDAO; // IMPORTAR
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import esfe.dominio.Categoria;

public class FinanceForm extends JPanel {

    private JDateChooser filtroDesde, filtroHasta;
    private JTable tabla;
    private JComboBox<String> categoriaCombo;

    public FinanceForm() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(30, 30, 30));
        UIManager.put("Button.arc", 10);

        initFiltros();
        initTabla();
        initBotonesCRUD();

        actualizarTabla();
    }


    private void initFiltros() {
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(new Color(40, 40, 40));
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        filtroDesde = new JDateChooser();
        filtroDesde.setDateFormatString("yyyy-MM-dd");
        filtroHasta = new JDateChooser();
        filtroHasta.setDateFormatString("yyyy-MM-dd");

        JButton filtrarBtn = new JButton("Filtrar");
        filtrarBtn.setBackground(new Color(75, 110, 175));
        filtrarBtn.setForeground(Color.WHITE);
        filtrarBtn.addActionListener(e -> filtrarMovimientos());

        // ✅ Cambiar la carga de categorías
        categoriaCombo = new JComboBox<>();
        categoriaCombo.setPreferredSize(new Dimension(120, 25));
        categoriaCombo.addItem("Todas");

        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        for (Categoria c : categorias) {
            categoriaCombo.addItem(c.getNombre());
        }

        JButton filtrarCategoriaBtn = new JButton("Filtrar por categoría");
        filtrarCategoriaBtn.setBackground(new Color(75, 110, 175));
        filtrarCategoriaBtn.setForeground(Color.WHITE);
        filtrarCategoriaBtn.addActionListener(e -> filtrarPorCategoria());

        filtroPanel.add(new JLabel("Desde:", JLabel.RIGHT));
        filtroPanel.add(filtroDesde);
        filtroPanel.add(new JLabel("Hasta:", JLabel.RIGHT));
        filtroPanel.add(filtroHasta);
        filtroPanel.add(filtrarBtn);
        filtroPanel.add(new JLabel("Categoría:", JLabel.RIGHT));
        filtroPanel.add(categoriaCombo);
        filtroPanel.add(filtrarCategoriaBtn);

        add(filtroPanel, BorderLayout.NORTH);
    }

    private void initTabla() {
        tabla = new JTable(new DefaultTableModel(new String[]{"ID", "Tipo", "Descripción", "Monto", "Fecha", "Categoría"}, 0));
        tabla.setRowHeight(24);
        tabla.setBackground(new Color(50, 50, 50));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(80, 80, 80));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);
    }

    private void initBotonesCRUD() {
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        crudPanel.setBackground(new Color(40, 40, 40));
        crudPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton editarBtn = new JButton("✏️ Editar");
        JButton eliminarBtn = new JButton("🗑️ Eliminar");

        estilizarBoton(editarBtn);
        estilizarBoton(eliminarBtn);

        editarBtn.addActionListener(e -> editarMovimiento());
        eliminarBtn.addActionListener(e -> eliminarMovimiento());

        crudPanel.add(editarBtn);
        crudPanel.add(eliminarBtn);

        add(crudPanel, BorderLayout.SOUTH);
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(75, 110, 175));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
    }

    private void editarMovimiento() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un movimiento para editar.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        int id = (int) model.getValueAt(fila, 0);
        String tipo = (String) model.getValueAt(fila, 1);
        String desc = (String) model.getValueAt(fila, 2);
        double monto = (double) model.getValueAt(fila, 3);
        String fecha = (String) model.getValueAt(fila, 4);
        String categoriaActual = (String) model.getValueAt(fila, 5); // ✅ Obtener la categoría actual

        String nuevoTipo = (String) JOptionPane.showInputDialog(this, "Tipo:", "Editar Movimiento",
                JOptionPane.PLAIN_MESSAGE, null, new String[]{"Ingreso", "Egreso"}, tipo);

        String nuevaDesc = JOptionPane.showInputDialog(this, "Descripción:", desc);
        String nuevoMontoStr = JOptionPane.showInputDialog(this, "Monto:", String.valueOf(monto));
        String nuevaFecha = JOptionPane.showInputDialog(this, "Fecha (yyyy-MM-dd):", fecha);

        // ✅ Agregar selección de categoría
        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        String[] nombresCategorias = categorias.stream().map(Categoria::getNombre).toArray(String[]::new);
        String nuevaCategoria = (String) JOptionPane.showInputDialog(this, "Categoría:", "Editar Movimiento",
                JOptionPane.PLAIN_MESSAGE, null, nombresCategorias, categoriaActual);

        try {
            double nuevoMonto = Double.parseDouble(nuevoMontoStr);

            // ✅ Buscar el ID de la nueva categoría
            int nuevoIdCategoria = categorias.stream()
                    .filter(c -> c.getNombre().equals(nuevaCategoria))
                    .findFirst()
                    .map(Categoria::getIdCategoria)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

            Movimiento actualizado = new Movimiento(nuevoTipo, nuevaDesc, nuevoMonto, nuevaFecha, nuevoIdCategoria);
            actualizado.id = id;

            MovimientoDAO.actualizarMovimiento(actualizado);
            actualizarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage());
        }
    }

    private void eliminarMovimiento() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un movimiento para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este movimiento?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        int id = (int) model.getValueAt(fila, 0);

        try {
            MovimientoDAO.eliminarMovimiento(id);
            actualizarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void filtrarMovimientos() {
        java.util.Date desdeUtil = filtroDesde.getDate();
        java.util.Date hastaUtil = filtroHasta.getDate();

        if (desdeUtil == null || hastaUtil == null) {
            JOptionPane.showMessageDialog(this, "Seleccione ambas fechas para filtrar.");
            return;
        }

        java.sql.Date desdeSql = new java.sql.Date(desdeUtil.getTime());
        java.sql.Date hastaSql = new java.sql.Date(hastaUtil.getTime());

        List<Movimiento> filtrados = MovimientoDAO.filtrarPorFecha(desdeSql, hastaSql);
        cargarMovimientosEnTabla(filtrados);
    }

    private void filtrarPorCategoria() {
        String categoriaSeleccionada = (String) categoriaCombo.getSelectedItem();
        if ("Todas".equals(categoriaSeleccionada)) {
            actualizarTabla();
        } else {
            List<Movimiento> filtrados = MovimientoDAO.filtrarPorCategoria(categoriaSeleccionada);
            cargarMovimientosEnTabla(filtrados);
        }
    }

    private void actualizarTabla() {
        List<Movimiento> lista = MovimientoDAO.obtenerMovimientos();
        cargarMovimientosEnTabla(lista);
    }

    private void cargarMovimientosEnTabla(List<Movimiento> lista) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        for (Movimiento m : lista) {
            model.addRow(new Object[]{m.id, m.tipo, m.descripcion, m.monto, m.fecha, m.nombreCategoria});
        }
    }
}
