package frontend.gui;

import frontend.controller.AuthController;
import frontend.controller.IssueController;
import frontend.exception.RequestError;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static frontend.gui.HomePage.setFlatLaf;

/**
 * Finestra principale per il Login e punto di ingresso dell'applicazione (Main Class).
 * <p>
 * Questa classe estende {@link JFrame} e fornisce l'interfaccia grafica per l'autenticazione degli utenti.
 * Le sue funzionalità principali includono:
 * </p>
 * <ul>
 * <li>Campi di input stilizzati per email e password con gestione dei placeholder.</li>
 * <li>Pulsante per mostrare/nascondere la password in chiaro.</li>
 * <li>Collegamento alla pagina di registrazione per nuovi utenti.</li>
 * <li>Integrazione con {@link AuthController} per la verifica delle credenziali.</li>
 * <li>Metodo {@code main} che configura il Look And Feel e avvia l'applicazione.</li>
 * </ul>
 */
public class LogInPage extends JFrame {

    /**
     * Textfield per la mail.
     */
    private JTextField emailField;

    /**
     * Textfield per la password.
     */
    private JPasswordField passwordField;

    /**
     * Dimensione standard per i campi di input.
     */
    private final Dimension INPUT_DIMENSION = new Dimension(300, 45);

    /**
     * Costruttore della pagina di Login.
     * <p>
     * Configura le proprietà della finestra (titolo, dimensioni fisse, chiusura),
     * inizializza il layout principale e richiama i metodi per costruire le varie sezioni
     * dell'interfaccia (Titolo, Input, Footer).
     * </p>
     */
    public LogInPage() {
        super("Login - BugBoard26");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); // Centra la finestra nello schermo
        this.setResizable(false);
        URL iconURL = getClass().getResource("/frontend/gui/images/applicationIcon.png");
        if (iconURL != null)
            this.setIconImage(new ImageIcon(iconURL).getImage());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        setupTitle(mainPanel);
        setupInputs(mainPanel);
        setupFooter(mainPanel);

        this.setContentPane(mainPanel);
    }

    /**
     * Configura e aggiunge il titolo dell'applicazione al pannello.
     *
     * @param panel Il pannello principale a cui aggiungere il titolo.
     */
    private void setupTitle(JPanel panel) {

        JLabel titleLabel = new JLabel("BugBoard26");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(ColorsList.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.1f, new Insets(0, 0, 30, 0));
        panel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    /**
     * Organizza l'area centrale contenente i campi di input.
     *
     * @param panel Il pannello principale.
     */
    private void setupInputs(JPanel panel) {
        JPanel inputsContainer = new JPanel(new GridBagLayout());
        inputsContainer.setOpaque(false);

        addLabeledInput(inputsContainer, 0, "Email:", "Inserisci la tua email...", false);

        addLabeledInput(inputsContainer, 1, "Password:", "Inserisci la tua password...", true);

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(0, 0, 30, 0));
        panel.add(inputsContainer, Constraints.getGridBagConstraints());
    }

    /**
     * Metodo helper per aggiungere una riga di input (Etichetta + Campo) al container.
     *
     * @param container   Il pannello contenitore degli input.
     * @param gridY       La riga della griglia dove posizionare i componenti.
     * @param labelText   Il testo dell'etichetta (es. "Email:").
     * @param placeholder Il testo segnaposto del campo.
     * @param isPassword  True se il campo deve essere mascherato (password), False altrimenti.
     */
    private void addLabeledInput(JPanel container, int gridY, String labelText, String placeholder, boolean isPassword) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(ColorsList.TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.RIGHT); // Allinea a destra vicino al campo

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
     * Crea il pannello stilizzato per l'inserimento dell'Email.
     *
     * @param placeholder Testo segnaposto.
     * @return Il pannello contenente il campo di testo.
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

        // Gestione automatica del focus per rimuovere/ripristinare il placeholder
        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(emailField, placeholder);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        wrapper.add(emailField, Constraints.getGridBagConstraints());

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);

        // Permette di fare login premendo INVIO nel campo email
        emailField.addActionListener(e -> performLogin());

        return cushion;
    }

    /**
     * Crea il pannello stilizzato per l'inserimento della Password.
     * <p>
     * Include un pulsante "Occhio" per commutare la visibilità della password e
     * gestisce manualmente il comportamento del placeholder in combinazione con il mascheramento dei caratteri.
     * </p>
     *
     * @param placeholder Testo segnaposto.
     * @return Il pannello contenente il campo password e il toggle di visibilità.
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

        // Inizialmente mostra il placeholder in chiaro (nessun echo char)
        passwordField.setEchoChar((char) 0);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(passwordField.getPassword());
                if (currentText.equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•'); // Attiva mascheramento all'inserimento
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0); // Ripristina testo in chiaro per il placeholder
                }
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        wrapper.add(passwordField, Constraints.getGridBagConstraints());

        // Pulsante "Occhio"
        JButton eyeButton = new JButton("👁");
        eyeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        eyeButton.setBorderPainted(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton.setForeground(Color.GRAY);
        eyeButton.setPreferredSize(new Dimension(30, 30));

        eyeButton.addActionListener(e -> {
            // Non fare nulla se c'è il placeholder
            if (new String(passwordField.getPassword()).equals(placeholder)) return;

            if (passwordField.getEchoChar() == '•') {
                passwordField.setEchoChar((char) 0); // Mostra password
                eyeButton.setForeground(ColorsList.PRIMARY_COLOR);
            } else {
                passwordField.setEchoChar('•'); // Nascondi password
                eyeButton.setForeground(Color.GRAY);
            }
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        wrapper.add(eyeButton, Constraints.getGridBagConstraints());

        JPanel cushion = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cushion.setOpaque(false);
        cushion.add(wrapper);

        // Permette di fare login premendo INVIO nel campo password
        passwordField.addActionListener(e -> performLogin());

        return cushion;
    }

    /**
     * Configura il piè di pagina con il pulsante di accesso e il link alla registrazione.
     *
     * @param panel Il pannello principale.
     */
    private void setupFooter(JPanel panel) {
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);

        // Pulsante Accedi
        JButton loginButton = new JButton("Accedi");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(ColorsList.PRIMARY_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.setPreferredSize(new Dimension(INPUT_DIMENSION.width + 50, 45));

        loginButton.addActionListener(e -> performLogin());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 20, 0));
        footerPanel.add(loginButton, Constraints.getGridBagConstraints());

        // Link Registrazione
        JLabel registerLabel = new JLabel("Non hai un account? Registrati");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerLabel.setForeground(Color.GRAY);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Gestione hover e click per la registrazione
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistrationPage().setVisible(true);
                dispose(); // Chiude la finestra di login
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Effetto hover: cambia colore e mette in grassetto
                registerLabel.setText("<html>Non hai un account? <font color='#0078D7'><b>Registrati</b></font></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setText("Non hai un account? Registrati");
            }
        });

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 0, 0, 0));
        footerPanel.add(registerLabel, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.1f, new Insets(0, 0, 0, 0));
        panel.add(footerPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di autenticazione.
     * <p>
     * 1. Recupera i dati dai campi.<br>
     * 2. Verifica che non siano vuoti o uguali ai placeholder.<br>
     * 3. Invoca {@link AuthController#login}.<br>
     * 4. Se il login ha successo, apre la {@link HomePage} e chiude questa finestra.<br>
     * 5. Altrimenti, mostra un messaggio di errore.
     * </p>
     */
    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String placeholderEmail = "Inserisci la tua email...";
        String placeholderPass = "Inserisci la tua password...";

        // Validazione base
        if (email.isEmpty() || email.equals(placeholderEmail) ||
                password.isEmpty() || password.equals(placeholderPass)) {
            JOptionPane.showMessageDialog(this, "Inserisci credenziali valide.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = AuthController.getInstance().login(email, password);

        if (success) {
            HomePage homePage = new HomePage();
            homePage.getMainFrame().setExtendedState(Frame.MAXIMIZED_BOTH); // Apre la home a schermo intero
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenziali non valide o errore di connessione.", "Errore Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Punto di ingresso principale dell'applicazione.
     * <p>
     * Configura il Look and Feel (FlatLaf), imposta un gestore globale per le eccezioni non catturate
     * (filtrando gli errori di richiesta attesi) e avvia l'interfaccia grafica nel thread AWT.
     * </p>
     *
     * @param args Argomenti da riga di comando (non utilizzati).
     */
    public static void main(String[] args) {

        setFlatLaf();
        final Logger logger = Logger.getLogger(LogInPage.class.getName());

        // Gestore globale per le eccezioni non catturate
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {

            // Ignora le eccezioni di tipo RequestError (spesso usate per controllo flusso o errori utente noti)
            if (throwable instanceof RequestError) {
                return;
            }

            logger.log(Level.SEVERE, throwable.getMessage());
        });

        SwingUtilities.invokeLater(() -> new LogInPage().setVisible(true));
    }
}