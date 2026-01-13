package frontend.gui;

import frontend.controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Finestra per la registrazione di nuovi utenti.
 * <p>
 * Questa classe estende {@link JFrame} e fornisce l'interfaccia per creare un nuovo account.
 * Le caratteristiche principali includono:
 * </p>
 * <ul>
 * <li>Campi per Email e Password con validazione e placeholder dinamici.</li>
 * <li>Integrazione con {@link AuthController} per inviare la richiesta di registrazione.</li>
 * <li>Navigazione per tornare alla pagina di Login se l'utente possiede già un account.</li>
 * </ul>
 * <p>
 * Nota: Questa pagina registra di default utenti con ruolo "User" (livello 0).
 * </p>
 */
public class RegistrationPage extends JFrame {

    /**
     * Textfield per la mail.
     */
    private JTextField emailField;

    /**
     * Textfield per la password.
     */
    private JPasswordField passwordField;

    // Costanti grafiche per mantenere coerenza stilistica
    /**
     * Dimensioni grafiche.
     */
    private final Dimension INPUT_DIMENSION = new Dimension(300, 45);

    /**
     * Colore principale della schermata.
     */
    private final Color PRIMARY_COLOR = new Color(0, 120, 215);

    /**
     * Colore principale del testo.
     */
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    /**
     * Costruttore della pagina di Registrazione.
     * <p>
     * Inizializza la finestra, imposta le dimensioni e costruisce l'interfaccia
     * richiamando i metodi di setup per Titolo, Input e Footer.
     * </p>
     */
    public RegistrationPage() {

        super("Registrazione - BugBoard26");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        setupTitle(mainPanel);
        setupInputs(mainPanel);
        setupFooter(mainPanel);

        this.setContentPane(mainPanel);
    }

    /**
     * Configura il titolo della pagina ("BugBoard26").
     *
     * @param panel Il pannello principale.
     */
    private void setupTitle(JPanel panel) {
        JLabel titleLabel = new JLabel("BugBoard26");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.1f, new Insets(0, 0, 30, 0));
        panel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il contenitore centrale per i campi di input.
     *
     * @param panel Il pannello principale.
     */
    private void setupInputs(JPanel panel) {
        JPanel inputsContainer = new JPanel(new GridBagLayout());
        inputsContainer.setOpaque(false);

        addLabeledInput(inputsContainer, 0, "Email:", "Inserisci la tua email...", false);

        addLabeledInput(inputsContainer, 1, "Password:", "Scegli una password...", true);

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(0, 0, 30, 0));
        panel.add(inputsContainer, Constraints.getGridBagConstraints());
    }

    /**
     * Metodo helper per aggiungere un campo di input con etichetta.
     *
     * @param container   Il pannello contenitore degli input.
     * @param gridY       La posizione verticale nella griglia.
     * @param labelText   Il testo dell'etichetta.
     * @param placeholder Il testo segnaposto.
     * @param isPassword  True se è un campo password, False se è email.
     */
    private void addLabeledInput(JPanel container, int gridY, String labelText, String placeholder, boolean isPassword) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        Constraints.setConstraints(0, gridY, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.0f, 0.0f, new Insets(0, 0, 15, 15));
        container.add(label, Constraints.getGridBagConstraints());

        JPanel inputWrapper;
        if (isPassword) {
            inputWrapper = createPasswordPanel(placeholder);
        } else {
            inputWrapper = createEmailPanel(placeholder);
        }

        Constraints.setConstraints(1, gridY, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.0f, 0.0f, new Insets(0, 0, 15, 0));
        container.add(inputWrapper, Constraints.getGridBagConstraints());
    }

    /**
     * Crea il pannello per l'input Email.
     * <p>
     * Include la logica per il placeholder (sparire al focus, riapparire se vuoto) e
     * collega il tasto Invio alla funzione di registrazione.
     * </p>
     */
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

        emailField.addActionListener(e -> performRegistration());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        wrapper.add(emailField, Constraints.getGridBagConstraints());

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);
        return cushion;
    }

    /**
     * Crea il pannello per l'input Password.
     * <p>
     * Gestisce il mascheramento dei caratteri, il placeholder e include il pulsante "Occhio"
     * per visualizzare la password in chiaro.
     * </p>
     */
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

        // Gestione manuale del placeholder per JPasswordField
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

        passwordField.addActionListener(e -> performRegistration());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        wrapper.add(passwordField, Constraints.getGridBagConstraints());

        // Bottone mostra/nascondi password
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

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);
        return cushion;
    }

    /**
     * Configura il piè di pagina con il pulsante di azione e il link al Login.
     */
    private void setupFooter(JPanel panel) {
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);

        JButton registerButton = new JButton("Registrati");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.setPreferredSize(new Dimension(INPUT_DIMENSION.width + 50, 45));

        registerButton.addActionListener(e -> performRegistration());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 20, 0));
        footerPanel.add(registerButton, Constraints.getGridBagConstraints());

        // Link per tornare al Login
        JLabel loginLabel = new JLabel("Hai già un account? Effettua il login");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLabel.setForeground(Color.GRAY);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LogInPage().setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setText("<html>Hai già un account? <font color='#0078D7'><b>Effettua il login</b></font></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setText("Hai già un account? Effettua il login");
            }
        });

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 0, 0));
        footerPanel.add(loginLabel, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.1f, new Insets(0, 0, 0, 0));
        panel.add(footerPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di registrazione.
     * <p>
     * 1. Recupera i dati dai campi.<br>
     * 2. Verifica che i campi non siano vuoti o uguali ai placeholder.<br>
     * 3. Invoca {@link AuthController#registration} passando ruolo 0 (Utente Standard).<br>
     * 4. Se successo, mostra messaggio di conferma e reindirizza al Login.<br>
     * 5. Altrimenti mostra errore.
     * </p>
     */
    private void performRegistration() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String placeholderEmail = "Inserisci la tua email...";
        String placeholderPass = "Scegli una password...";

        if (email.isEmpty() || email.equals(placeholderEmail) ||
                password.isEmpty() || password.equals(placeholderPass)) {
            JOptionPane.showMessageDialog(this, "Compila tutti i campi per registrarti.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Il parametro '0' indica la creazione di un utente standard (USER)
        boolean success = AuthController.getInstance().registration(email, password, 0);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!\nOra verrai reindirizzato alla pagina di login.", "Benvenuto", JOptionPane.INFORMATION_MESSAGE);

            LogInPage logInPage = new  LogInPage();
            logInPage.setVisible(true);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Credenziali già in uso / non valide o errore di connessione.", "Errore Registrazione", JOptionPane.ERROR_MESSAGE);
        }
    }
}