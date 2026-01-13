package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finestra di dialogo per la creazione di un nuovo team.
 * <p>
 * Questa classe estende {@link JDialog} e fornisce un'interfaccia grafica per permettere all'utente
 * di inserire il nome di un nuovo team da associare al progetto corrente.
 * Gestisce la validazione dell'input e invoca il {@link TeamController} per la persistenza dei dati.
 * </p>
 */
public class CreateTeamDialog extends JDialog {

    /**
     * Logger per la registrazione degli eventi di creazione team.
     */
    static final Logger logger = Logger.getLogger(CreateTeamDialog.class.getName());

    /**
     * Costruttore della finestra di dialogo.
     * <p>
     * Configura il layout, aggiunge l'etichetta, il campo di testo per il nome del team
     * e il pulsante di conferma. La finestra è modale rispetto al frame proprietario.
     * </p>
     *
     * @param owner Il frame principale dell'applicazione che funge da genitore per questo dialogo.
     */
    public CreateTeamDialog(JFrame owner) {
        super(owner, "Nuovo Team", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Inserisci il nome del nuovo team:");
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
     * Crea e configura il pulsante di conferma per la creazione del team.
     * <p>
     * Definisce l'azione da eseguire al click:
     * <ol>
     * <li>Verifica che il nome inserito non sia vuoto.</li>
     * <li>Invoca {@link TeamController#createTeam(String)} per creare il team.</li>
     * <li>In caso di successo, mostra un messaggio di conferma, registra l'evento nel log (includendo l'ID del progetto corrente) e chiude la finestra.</li>
     * <li>In caso di nome vuoto, mostra un errore locale tramite {@link FloatingMessage}.</li>
     * </ol>
     * </p>
     *
     * @param nameField Il componente di testo da cui leggere il nome del team.
     * @return Il pulsante configurato e pronto all'uso.
     */
    private JButton getConfirmBtn(JTextField nameField) {
        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(0, 120, 215)); // Blu primario
        confirmBtn.setForeground(Color.WHITE);

        confirmBtn.addActionListener(e -> {
            String teamName = nameField.getText().trim();
            if (!teamName.isEmpty()) {
                boolean success = TeamController.getInstance().createTeam(teamName);

                if(!success)
                    return;

                // Nota: Il messaggio "Progetto creato" potrebbe essere un refuso nel codice originale (dovrebbe essere "Team creato"),
                // ma viene mantenuto fedele al codice sorgente fornito.
                JOptionPane.showMessageDialog(this, "Progetto creato con successo!", "Creazione avvenuta", JOptionPane.INFORMATION_MESSAGE);
                logger.log(Level.FINE, "Team creato: {0}, per Progetto con ID: {1}", new Object[]{teamName, ProjectController.getInstance().getProject().getId()});
                this.dispose();

            } else {
                new FloatingMessage("Il nome del team è obbligatorio", confirmBtn, FloatingMessage.ERROR_MESSAGE);
            }
        });
        return confirmBtn;
    }
}