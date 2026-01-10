package frontend.gui;

import frontend.controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DevOrAdminCreationDialog extends JDialog {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox adminCheckBox;

    private final Dimension INPUT_DIMENSION = new Dimension(350, 50);
    private final Color PRIMARY_COLOR = new Color(0, 120, 215);

    public DevOrAdminCreationDialog(JFrame owner) {
        super(owner, "Crea Nuova Utenza", true);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        setupTitle(mainPanel);
        setupInputs(mainPanel);
        setupFooter(mainPanel);

        this.setContentPane(mainPanel);
        this.setSize(450, 500);
        this.setLocationRelativeTo(owner);
        this.setResizable(false);
    }

    private void setupTitle(JPanel panel) {
        JLabel titleLabel = new JLabel("Crea Utenza");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(0, 0, 30, 0));
        panel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    private void setupInputs(JPanel panel) {
        JPanel inputsContainer = new JPanel(new GridBagLayout());
        inputsContainer.setOpaque(false);

        JPanel emailPanel = createEmailPanel("Email utente...");
        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 15, 0));
        inputsContainer.add(emailPanel, Constraints.getGridBagConstraints());

        JPanel passwordPanel = createPasswordPanel("Inserisci password...");
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 15, 0));
        inputsContainer.add(passwordPanel, Constraints.getGridBagConstraints());

        adminCheckBox = new JCheckBox("Conferisci privilegi di Amministratore");
        adminCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminCheckBox.setForeground(new Color(80, 80, 80));
        adminCheckBox.setOpaque(false);
        adminCheckBox.setFocusPainted(false);

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.0f, 0.0f, new Insets(0, 10, 0, 0));
        inputsContainer.add(adminCheckBox, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(0, 0, 20, 0));
        panel.add(inputsContainer, Constraints.getGridBagConstraints());
    }

    private JPanel createEmailPanel(String placeholder) {

        RoundedPanel wrapper = new RoundedPanel(new GridBagLayout());
        wrapper.setPreferredSize(INPUT_DIMENSION);
        wrapper.setMinimumSize(INPUT_DIMENSION);
        wrapper.setMaximumSize(INPUT_DIMENSION);
        wrapper.setBackground(Color.WHITE);
        wrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);
        wrapper.setBorder(new EmptyBorder(5, 10, 5, 10));

        emailField = new JTextField(placeholder);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createEmptyBorder());
        emailField.setOpaque(false);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(emailField, placeholder);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        wrapper.add(emailField, Constraints.getGridBagConstraints());

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);
        return cushion;
    }

    private JPanel createPasswordPanel(String placeholder) {

        RoundedPanel wrapper = new RoundedPanel(new GridBagLayout());
        wrapper.setPreferredSize(INPUT_DIMENSION);
        wrapper.setMinimumSize(INPUT_DIMENSION);
        wrapper.setMaximumSize(INPUT_DIMENSION);
        wrapper.setBackground(Color.WHITE);
        wrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);
        wrapper.setBorder(new EmptyBorder(5, 10, 5, 5));

        passwordField = new JPasswordField(placeholder);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setOpaque(false);
        passwordField.setEchoChar((char) 0);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(passwordField.getPassword());
                if (currentText.equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        wrapper.add(passwordField, Constraints.getGridBagConstraints());

        JButton eyeButton = new JButton("👁");
        eyeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        eyeButton.setBorderPainted(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton.setForeground(Color.GRAY);
        eyeButton.setPreferredSize(new Dimension(30, 30));

        eyeButton.addActionListener(e -> {
            if (new String(passwordField.getPassword()).equals(placeholder)) return;
            if (passwordField.getEchoChar() == '•') {
                passwordField.setEchoChar((char) 0);
                eyeButton.setForeground(PRIMARY_COLOR);
            } else {
                passwordField.setEchoChar('•');
                eyeButton.setForeground(Color.GRAY);
            }
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        wrapper.add(eyeButton, Constraints.getGridBagConstraints());

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);
        return cushion;
    }

    private void setupFooter(JPanel panel) {
        JButton createButton = new JButton("Crea Utenza");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createButton.setForeground(Color.WHITE);
        createButton.setBackground(PRIMARY_COLOR);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setPreferredSize(new Dimension(200, 45));

        createButton.addActionListener(e -> performCreation());

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(10, 0, 0, 0));
        panel.add(createButton, Constraints.getGridBagConstraints());
    }

    private void performCreation() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || email.equals("Email utente...") ||
                password.isEmpty() || password.equals("Password temporanea...")) {
            JOptionPane.showMessageDialog(this, "Compila tutti i campi.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int role = adminCheckBox.isSelected() ? 2 : 1;


        boolean success = AuthController.getInstance().registration(email, password, role);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Utente creato con successo!\nRuolo: " + (role == 2 ? "Admin" : "Developer"),
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Errore nella creazione dell'utente.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}