package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dialogo di conferma per l'aggiunta di un nuovo membro al team.
 * <p>
 * Mostra un messaggio di conferma con l'email dell'utente selezionato.
 * Gestisce l'interazione con il {@link TeamController} per eseguire effettivamente l'operazione
 * e fornisce feedback visivo in caso di successo o errore (es. utente già presente).
 * </p>
 */
public class ConfirmAddDialog extends JDialog {

    /**
     * Logger per la registrazione degli eventi di aggiunta.
     */
    static final Logger logger = Logger.getLogger(ConfirmAddDialog.class.getName());

    /**
     * Costruttore della finestra di dialogo.
     * <p>
     * Crea un pannello con un'etichetta che mostra l'email dell'utente da aggiungere
     * e un pulsante di conferma.
     * </p>
     *
     * @param owner        Il frame proprietario del dialogo.
     * @param email        L'indirizzo email dell'utente da aggiungere.
     * @param parentDialog Il dialogo padre ({@link AddMemberDialog}) da chiudere in caso di successo.
     */
    public ConfirmAddDialog(JFrame owner, String email, AddMemberDialog parentDialog) {
        super(owner, "Conferma Aggiunta", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Aggiungere " + email + " al team?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JButton confirmBtn = getConfirmBtn(email, parentDialog);

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }

    /**
     * Crea e configura il pulsante di conferma.
     * <p>
     * Aggiunge un ActionListener che:
     * <ol>
     * <li>Invoca {@link TeamController#addMemberToSelectedTeam} per aggiungere il membro.</li>
     * <li>Controlla il codice di ritorno (presunto: 0=successo, 1=già presente, 2=errore generico/annulla).</li>
     * <li>Se l'utente è già nel team, mostra un {@link FloatingMessage} di errore.</li>
     * <li>Se l'aggiunta ha successo, chiude sia questo dialogo che quello di ricerca padre.</li>
     * </ol>
     * </p>
     *
     * @param email        L'email dell'utente da aggiungere.
     * @param parentDialog Il dialogo padre da chiudere in caso di successo.
     * @return Il pulsante configurato.
     */
    private JButton getConfirmBtn(String email, AddMemberDialog parentDialog) {

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(40, 167, 69));
        confirmBtn.setForeground(Color.WHITE);

        confirmBtn.addActionListener(e -> {

            Integer success = TeamController.getInstance().addMemberToSelectedTeam(email);

            if(success == 2)
                return;

            if(success == 1){

                new FloatingMessage("L'utente è già nel team!", confirmBtn, FloatingMessage.ERROR_MESSAGE);

                return;
            }

            logger.log(Level.FINE, "Aggiunto utente: {0}, al team con ID: {1}", new Object[]{email, TeamController.getInstance().getTeam().getId()});

            parentDialog.dispose();

            dispose();
        });
        return confirmBtn;
    }
}