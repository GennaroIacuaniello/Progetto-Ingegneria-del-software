package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finestra di dialogo per la creazione di un nuovo progetto.
 * <p>
 * Questa classe estende {@link JDialog} e fornisce un'interfaccia semplice composta da
 * un'etichetta, un campo di testo e un pulsante di conferma.
 * Gestisce la validazione dell'input (nome non vuoto) e invoca il {@link ProjectController}
 * per effettuare la creazione sul backend.
 * </p>
 */
public class CreateProjectDialog extends JDialog {

    /**
     * Logger per la registrazione degli eventi di creazione progetto.
     */
    static final Logger logger = Logger.getLogger(CreateProjectDialog.class.getName());

    /**
     * Costruttore della finestra di dialogo.
     * <p>
     * Inizializza il pannello principale, imposta il layout e aggiunge i componenti grafici
     * (label, campo di testo, bottone). La finestra è impostata come modale rispetto al frame proprietario.
     * </p>
     *
     * @param owner Il frame principale dell'applicazione che possiede questo dialogo.
     */
    public CreateProjectDialog(JFrame owner) {

        super(owner, "Nuovo Progetto", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Inserisci il nome del nuovo progetto:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.WEST, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 10, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JTextField nameField = new JTextField(20);
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(nameField, Constraints.getGridBagConstraints());

        JButton confirmBtn = getConfirmBtn(nameField);

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }

    /**
     * Crea e configura il pulsante di conferma.
     * <p>
     * Definisce l'ActionListener che esegue la logica di creazione:
     * <ol>
     * <li>Recupera il testo dal campo e rimuove gli spazi vuoti.</li>
     * <li>Se il nome è vuoto, mostra un errore locale ({@link FloatingMessage}).</li>
     * <li>Se valido, invoca {@link ProjectController#createProject(String)}.</li>
     * <li>In caso di successo, mostra un messaggio di conferma, logga l'evento e chiude il dialogo.</li>
     * </ol>
     * </p>
     *
     * @param nameField Il campo di testo da cui leggere il nome del progetto.
     * @return Il pulsante configurato.
     */
    private JButton getConfirmBtn(JTextField nameField) {

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(0, 120, 215)); // Blu primario
        confirmBtn.setForeground(Color.WHITE);

        confirmBtn.addActionListener(e -> {
            String projectName = nameField.getText().trim();
            if (!projectName.isEmpty()) {

                boolean success = ProjectController.getInstance().createProject(projectName);

                if(!success)
                    return;

                JOptionPane.showMessageDialog(this, "Progetto creato con successo!", "Creazione avvenuta", JOptionPane.INFORMATION_MESSAGE);
                logger.log(Level.FINE, "Progetto creato: {0}", projectName);
                this.dispose();

            } else {
                new FloatingMessage("Il nome del progetto è obbligatorio", confirmBtn, FloatingMessage.ERROR_MESSAGE);
            }
        });
        return confirmBtn;
    }
}