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

    // Colores personalizados
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color CARD_COLOR = new Color(30, 30, 30);
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color PLACEHOLDER_COLOR = new Color(120, 120, 120);

    static {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Personalización de FlatLaf
            UIManager.put("Button.arc", 8);
            UIManager.put("TextComponent.arc", 5);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.arrowType", "triangle");
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("Button.innerFocusWidth", 1);
        } catch (Exception ex) {
            System.err.println("Error al cargar FlatLaf Dark: " + ex);
        }
    }

    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();

        initComponents();
        setupLayout();
        setupActions();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Panel de contenido (para efecto de tarjeta)
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));

        // Título
        JLabel lblTitle = new JLabel("Iniciar Sesión");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Campo Email
        JLabel lblEmail = new JLabel("Correo electrónico");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmail.setForeground(PLACEHOLDER_COLOR);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setForeground(TEXT_COLOR);
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Campo Contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPassword.setForeground(PLACEHOLDER_COLOR);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPassword.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(TEXT_COLOR);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnSalir = new JButton("Salir");
        styleButton(btnSalir, false);

        btnLogin = new JButton("Iniciar Sesión");
        styleButton(btnLogin, true);

        buttonPanel.add(btnSalir);
        buttonPanel.add(btnLogin);

        // Construcción de la interfaz
        cardPanel.add(lblTitle);
        cardPanel.add(lblEmail);
        cardPanel.add(txtEmail);
        cardPanel.add(lblPassword);
        cardPanel.add(txtPassword);
        cardPanel.add(buttonPanel);

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(cardPanel);
    }

    private void styleButton(JButton button, boolean isPrimary) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            button.setBackground(ACCENT_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        } else {
            button.setBackground(new Color(60, 60, 60));
            button.setForeground(TEXT_COLOR);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }
    }

    private void setupLayout() {
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Inicio de Sesión");
        setSize(400, 400);
        setLocationRelativeTo(mainForm);
        setResizable(false);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void setupActions() {
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
                JOptionPane.showMessageDialog(null,
                        "Credenciales incorrectas. Por favor, intente nuevamente.",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error: " + ex.getMessage(),
                    "Error del sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}