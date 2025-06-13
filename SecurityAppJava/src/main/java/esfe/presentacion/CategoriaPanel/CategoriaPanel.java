package esfe.presentacion.CategoriaPanel;

import esfe.dominio.Categoria;
import esfe.persistencia.CategoriaDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class CategoriaPanel extends JPanel {

    private JTextField nombreField;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaCategorias;
    private JButton editarBtn, eliminarBtn, agregarBtn;

    public CategoriaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(43, 43, 43));
        setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                new LineBorder(new Color(60, 60, 60), 2, true)
        ));

        JLabel titulo = new JLabel("📂 Gestión de Categorías", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        add(titulo, BorderLayout.NORTH);

        // Sección de ingreso de nueva categoría
        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        formPanel.setBackground(getBackground());

        nombreField = new JTextField();
        nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(new JLabel("Nueva categoría:", JLabel.LEFT), BorderLayout.NORTH);
        formPanel.add(nombreField, BorderLayout.CENTER);

        agregarBtn = new JButton("➕ Agregar Categoría");
        agregarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        agregarBtn.setBackground(new Color(40, 167, 69)); // Verde para indicar acción positiva
        agregarBtn.setForeground(Color.WHITE);
        formPanel.add(agregarBtn, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaCategorias = new JList<>(modeloLista);
        listaCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaCategorias.setBackground(new Color(60, 60, 60));
        listaCategorias.setForeground(Color.WHITE);
        listaCategorias.setBorder(new LineBorder(new Color(80, 80, 80), 1, true));
        JScrollPane scrollPane = new JScrollPane(listaCategorias);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(80, 80, 80), 1, true),
                "Categorías existentes",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.WHITE
        ));

        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones Editar y Eliminar
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonesPanel.setBackground(getBackground());

        editarBtn = new JButton("✏️ Editar");
        eliminarBtn = new JButton("🗑️ Eliminar");

        editarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        botonesPanel.add(editarBtn);
        botonesPanel.add(eliminarBtn);
        add(botonesPanel, BorderLayout.SOUTH);

        // Eventos
        agregarBtn.addActionListener(e -> agregarCategoria());
        editarBtn.addActionListener(e -> editarCategoria());
        eliminarBtn.addActionListener(e -> eliminarCategoria());

        cargarCategorias();
    }

    private void agregarCategoria() {
        String nombre = nombreField.getText().trim();
        if (!nombre.isEmpty()) {
            Categoria nueva = new Categoria(0, nombre);
            boolean exito = CategoriaDAO.agregarCategoria(nueva);
            if (exito) {
                cargarCategorias();
                nombreField.setText("");
                JOptionPane.showMessageDialog(this, "Categoría agregada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar la categoría.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para la categoría.");
        }
    }

    private void editarCategoria() {
        String nombreSeleccionado = listaCategorias.getSelectedValue();
        if (nombreSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para editar.");
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", nombreSeleccionado);
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return;
        }

        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        int idCategoria = categorias.stream()
                .filter(c -> c.getNombre().equals(nombreSeleccionado))
                .findFirst()
                .map(Categoria::getIdCategoria)
                .orElse(-1);

        if (idCategoria != -1) {
            Categoria actualizada = new Categoria(idCategoria, nuevoNombre);
            if (CategoriaDAO.actualizarCategoria(actualizada)) {
                cargarCategorias();
                JOptionPane.showMessageDialog(this, "Categoría actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la categoría.");
            }
        }
    }

    private void eliminarCategoria() {
        String nombreSeleccionado = listaCategorias.getSelectedValue();
        if (nombreSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar esta categoría?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        List<Categoria> categorias = CategoriaDAO.obtenerCategorias();
        int idCategoria = categorias.stream()
                .filter(c -> c.getNombre().equals(nombreSeleccionado))
                .findFirst()
                .map(Categoria::getIdCategoria)
                .orElse(-1);

        if (idCategoria != -1) {
            if (CategoriaDAO.eliminarCategoria(idCategoria)) {
                cargarCategorias();
                JOptionPane.showMessageDialog(this, "Categoría eliminada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la categoría.");
            }
        }
    }

    private void cargarCategorias() {
        modeloLista.clear();
        List<Categoria> lista = CategoriaDAO.obtenerCategorias();
        for (Categoria c : lista) {
            modeloLista.addElement(c.getNombre());
        }
    }
}
