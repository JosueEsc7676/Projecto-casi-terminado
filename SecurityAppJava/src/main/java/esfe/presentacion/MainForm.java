package esfe.presentacion;

import esfe.dominio.Movimiento;
import esfe.dominio.User;
import esfe.persistencia.MovimientoDAO;
import com.formdev.flatlaf.FlatDarkLaf;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import esfe.presentacion.FinanceForm;

public class MainForm extends JFrame {

    private User userAutenticate;

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm() {
        UIManager.put("Panel.background", new Color(43, 43, 43)); // Fondo más moderno

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el estilo FlatLaf");
        }

        setTitle("💼 Simulador de Finanzas Domésticas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        UIManager.put("Panel.background", new Color(43, 43, 43)); // Fondo más moderno

        initUI();
    }

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        setJMenuBar(menuBar);
        UIManager.put("Panel.background", new Color(43, 43, 43)); // Fondo más moderno

        // 🔹 PERFIL
        JMenu menuPerfil = new JMenu("👤 Perfil");
        menuBar.add(menuPerfil);

        menuPerfil.add(createMenuItem("Cambiar contraseña", e -> new ChangePasswordForm(this).setVisible(true)));
        menuPerfil.add(createMenuItem("Cambiar de usuario", e -> new LoginForm(this).setVisible(true)));
        menuPerfil.add(createMenuItem("Salir", e -> System.exit(0)));

        // 🔹 MANTENIMIENTOS
        JMenu menuMantenimiento = new JMenu("⚙️ Mantenimientos");
        menuBar.add(menuMantenimiento);
        menuMantenimiento.add(createMenuItem("Usuarios", e -> new UserReadingForm(this).setVisible(true)));

        // 🔹 FINANZAS
        JMenu menuFinanzas = new JMenu("💰 Finanzas");
        menuBar.add(menuFinanzas);

        menuFinanzas.add(createMenuItem("Control de Finanzas", e -> openWindow("📊 Control de Finanzas", new FinancePanel(), 800, 600)));
        menuFinanzas.add(createMenuItem("Filtrar por fechas", e -> openWindow("📊 Filtrar por fechas", new FinanceForm(), 900, 700)));
    }

    private JMenuItem createMenuItem(String title, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(action);
        return item;
    }

    private void openWindow(String title, JPanel panel, int width, int height) {
        JFrame ventana = new JFrame(title);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(width, height);
        ventana.setLocationRelativeTo(null);
        ventana.add(panel);
        ventana.setVisible(true);
    }

    public class FinancePanel extends JPanel {

        private JTextField descripcionField, montoField;
        private JDateChooser dateChooser;
        private JComboBox<String> tipoBox;
        private JTable tabla;
        private JLabel resumenLabel, consejoLabel;

        public FinancePanel() {
            setLayout(new BorderLayout(15, 15));
            setBorder(new EmptyBorder(15, 15, 15, 15));
            setBackground(UIManager.getColor("Panel.background"));

            Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
            Font fuenteBold = new Font("Segoe UI", Font.BOLD, 14);

            // Panel superior: formulario
            JPanel topPanel = new JPanel(new GridLayout(2, 5, 10, 10));
            topPanel.setBorder(BorderFactory.createTitledBorder("➕ Agregar Movimiento"));
            topPanel.setBackground(getBackground());

            tipoBox = new JComboBox<>(new String[]{"Ingreso", "Egreso"});
            descripcionField = new JTextField();
            montoField = new JTextField();
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("yyyy-MM-dd");
            dateChooser.setDate(new Date());

            JButton agregarBtn = new JButton("Agregar");

            tipoBox.setFont(fuente);
            descripcionField.setFont(fuente);
            montoField.setFont(fuente);
            dateChooser.setFont(fuente);
            agregarBtn.setFont(fuenteBold);

            topPanel.add(new JLabel("Tipo:", JLabel.RIGHT));
            topPanel.add(new JLabel("Descripción:", JLabel.RIGHT));
            topPanel.add(new JLabel("Monto:", JLabel.RIGHT));
            topPanel.add(new JLabel("Fecha:", JLabel.RIGHT));
            topPanel.add(new JLabel(""));

            topPanel.add(tipoBox);
            topPanel.add(descripcionField);
            topPanel.add(montoField);
            topPanel.add(dateChooser);
            topPanel.add(agregarBtn);

            add(topPanel, BorderLayout.NORTH);

            // Tabla de movimientos
            tabla = new JTable(new DefaultTableModel(new String[]{"ID", "Tipo", "Descripción", "Monto", "Fecha"}, 0));
            tabla.setFont(fuente);
            tabla.setRowHeight(24);
            tabla.setFillsViewportHeight(true);
            tabla.setSelectionBackground(new Color(75, 110, 175));
            tabla.setGridColor(new Color(80, 80, 80));
            tabla.getTableHeader().setFont(fuenteBold);
            tabla.getTableHeader().setBackground(new Color(40, 40, 40));
            tabla.getTableHeader().setForeground(Color.WHITE);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createTitledBorder("📑 Historial de Movimientos"));
            add(scroll, BorderLayout.CENTER);

            // Panel inferior: resumen + consejo
            JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
            bottomPanel.setBorder(BorderFactory.createTitledBorder("📋 Resumen"));
            bottomPanel.setBackground(getBackground());

            resumenLabel = new JLabel("Resumen: ");
            consejoLabel = new JLabel("Consejo: ");
            resumenLabel.setFont(fuenteBold);
            consejoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            resumenLabel.setForeground(new Color(102, 255, 178));
            consejoLabel.setForeground(Color.LIGHT_GRAY);

            bottomPanel.add(resumenLabel);
            bottomPanel.add(consejoLabel);
            add(bottomPanel, BorderLayout.SOUTH);

            // Acción del botón Agregar
            agregarBtn.addActionListener(e -> {
                try {
                    String tipo = (String) tipoBox.getSelectedItem();
                    String desc = descripcionField.getText();
                    double monto = Double.parseDouble(montoField.getText());
                    Date fechaSeleccionada = dateChooser.getDate();

                    if (fechaSeleccionada == null) {
                        JOptionPane.showMessageDialog(this, "Seleccione una fecha válida", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String fecha = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionada);

                    Movimiento m = new Movimiento(tipo, desc, monto, fecha);
                    MovimientoDAO.agregarMovimiento(m);
                    actualizarTabla();
                    mostrarResumen();
                    mostrarConsejo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            actualizarTabla();
            mostrarResumen();
            mostrarConsejo();
        }

        private void actualizarTabla() {
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);
            List<Movimiento> lista = MovimientoDAO.obtenerMovimientos();
            for (Movimiento m : lista) {
                model.addRow(new Object[]{m.id, m.tipo, m.descripcion, m.monto, m.fecha});
            }
        }

        private void mostrarResumen() {
            double ingresos = MovimientoDAO.calcularTotalPorTipo("Ingreso");
            double egresos = MovimientoDAO.calcularTotalPorTipo("Egreso");
            resumenLabel.setText("Resumen → Ingresos: $" + ingresos + " | Egresos: $" + egresos);
        }

        private void mostrarConsejo() {
            double ingresos = MovimientoDAO.calcularTotalPorTipo("Ingreso");
            double egresos = MovimientoDAO.calcularTotalPorTipo("Egreso");
            String consejo = "Buen trabajo.";
            if (egresos > ingresos) {
                consejo = "¡Cuidado! Estás gastando más de lo que ganas.";
            } else if (ingresos - egresos > 500) {
                consejo = "¡Genial! Considera ahorrar o invertir.";
            }
            consejoLabel.setText("Consejo: " + consejo);
        }
    }
}
