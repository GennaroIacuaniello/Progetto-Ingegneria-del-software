package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dialogo di conferma per la rimozione di un membro da un team.
 * <p>
 * Mostra una finestra modale che chiede all'utente se è sicuro di voler rimuovere
 * uno specifico membro (identificato dall'email) dal team selezionato.
 * Gestisce l'interazione con il {@link TeamController} per eseguire l'operazione
 * e aggiorna la finestra padre in caso di successo.
 * </p>
 */
public class ConfirmDeleteMemberDialog extends JDialog {

    /**
     * Logger per registrare gli eventi di rimozione.
     */
    static final Logger logger = Logger.getLogger(ConfirmDeleteMemberDialog.class.getName());

    /**
     * Costruttore della finestra di dialogo.
     * <p>
     * Inizializza il pannello con il messaggio di conferma e il pulsante di azione.
     * </p>
     *
     * @param owner        Il frame principale che possiede questo dialogo.
     * @param email        L'email dell'utente che si sta tentando di rimuovere.
     * @param parentDialog Il dialogo padre ({@link ManageMembersDialog}) da aggiornare dopo la rimozione.
     */
    public ConfirmDeleteMemberDialog(JFrame owner, String email, ManageMembersDialog parentDialog) {

        super(owner, "Conferma Rimozione", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Rimuovere l'utente " + email + " dal team?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Constraints.setConstraints(0, 0, 2, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JButton confirmBtn = getConfirmBtn(email, parentDialog);

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }

    /**
     * Crea e configura il pulsante di conferma rimozione.
     * <p>
     * Il pulsante è stilizzato in rosso per indicare un'azione distruttiva.
     * Al click:
     * <ol>
     * <li>Invoca {@link TeamController#removeMemberFromSelectedTeam} per rimuovere il membro.</li>
     * <li>Se l'operazione ha successo, logga l'evento.</li>
     * <li>Aggiorna la lista dei membri nella finestra padre ({@code parentDialog.performSearch()}).</li>
     * <li>Chiude il dialogo di conferma.</li>
     * </ol>
     * </p>
     *
     * @param email        L'email dell'utente da rimuovere.
     * @param parentDialog Il dialogo padre da aggiornare.
     * @return Il pulsante configurato.
     */
    private JButton getConfirmBtn(String email, ManageMembersDialog parentDialog) {

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(220, 53, 69)); // Rosso per azione pericolosa
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));


        confirmBtn.addActionListener(e -> {

            boolean success = TeamController.getInstance().removeMemberFromSelectedTeam(email);

            if(!success)
                return;

            logger.log(Level.FINE, "Rimosso utente: {0}, dal team con ID: {1}", new Object[]{email, TeamController.getInstance().getTeam().getId()});

            // Aggiorna la vista della finestra padre per riflettere la rimozione
            parentDialog.performSearch();

            dispose();
        });
        return confirmBtn;
    }
}