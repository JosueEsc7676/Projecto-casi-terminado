package esfe.presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.formdev.flatlaf.FlatDarkLaf;

import esfe.dominio.User;
import esfe.persistencia.UserDAO;

public class LoginForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;

    private UserDAO userDAO;
    private MainForm mainForm;

    static {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 10); // Bordes redondeados
            UIManager.put("Component.focusWidth", 1); // Borde sutil en el foco
            UIManager.put("Panel.background", new Color(43, 43, 43)); // Fondo más moderno
        } catch (Exception ex) {
            System.err.println("Error al cargar FlatLaf Dark: " + ex);
        }
    }

    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();

        // Inicialización de componentes
        mainPanel = new JPanel();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");
        btnSalir = new JButton("Salir");

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(mainForm);
        setResizable(false);

        // Estilos y Layout
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(20, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtEmail.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        txtPassword.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        btnLogin.setFocusPainted(false);
        btnSalir.setFocusPainted(false);

        // Etiqueta Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblEmail);

        // Campo Email
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(txtEmail);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblPassword);

        // Campo Contraseña
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(txtPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(mainPanel.getBackground());
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSalir);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(buttonPanel);

        // Acciones
        btnSalir.addActionListener(e -> System.exit(0));
        btnLogin.addActionListener(e -> login());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login() {
        try {
            User user = new User();
            user.setEmail(txtEmail.getText());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            User userAut = userDAO.authenticate(user);

            if (userAut != null && userAut.getId() > 0 && userAut.getEmail().equals(user.getEmail())) {
                this.mainForm.setUserAutenticate(userAut);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Email y password incorrecto", "Login", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}
