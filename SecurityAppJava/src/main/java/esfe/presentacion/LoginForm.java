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
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(mainForm);
        setResizable(false);

        mainPanel.setBackground(new Color(20, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtEmail.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        txtPassword.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));

        btnLogin.setFocusPainted(false);
        btnSalir.setFocusPainted(false);

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
