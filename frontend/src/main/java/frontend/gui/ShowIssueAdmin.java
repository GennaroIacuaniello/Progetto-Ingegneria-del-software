package frontend.gui;

import frontend.controller.IssueController;
import frontend.controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista di dettaglio di un'issue specifica per il ruolo Amministratore.
 * <p>
 * Questa classe estende {@link ShowReportedIssueDeveloper} per visualizzare tutte le informazioni tecniche
 * della segnalazione.
 * <br>
 * La funzionalità esclusiva di questa classe è la <b>Gestione dell'Assegnazione</b>:
 * se l'issue visualizzata si trova nello stato "TODO" (ovvero non ha ancora un assegnatario),
 * viene mostrato un pannello di ricerca che permette all'admin di cercare uno sviluppatore
 * (tramite email) e assegnargli direttamente la risoluzione del problema.
 * </p>
 */
public class ShowIssueAdmin extends ShowReportedIssueDeveloper {

    // Componenti per la ricerca dello sviluppatore da assegnare
    private RoundedPanel tmpPanel;
    private JTextField searchField;
    private IconButton searchButton;
    private static final String SEARCHFIELD_PLACEHOLDER = "Inserisci email developer per assegnare";

    /**
     * Costruttore della vista issue Admin.
     * <p>
     * 1. Richiama il costruttore della superclasse per disegnare la scheda dati completa.<br>
     * 2. Controlla lo stato dell'issue: se è "TODO", costruisce e mostra l'interfaccia di assegnazione
     * (pannello, campo di testo, bottone). Se è già assegnata o risolta, questi componenti non vengono creati.
     * </p>
     *
     * @param parent Il frame genitore.
     */
    public ShowIssueAdmin(JFrame parent) {

        super(parent);

        // Mostra i controlli di assegnazione solo se l'issue è ancora in attesa (TODO)
        if (statusLabel.getText().equals("Stato: TODO")) {

            setTmpPanel();
            setSearchButton();
            setSearchField();
        }
    }

    /**
     * Configura il pannello contenitore per la barra di ricerca assegnatario.
     */
    private void setTmpPanel() {

        tmpPanel = new RoundedPanel(new GridBagLayout());

        tmpPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 5, 4, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(5, 5, 5, 5));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pulsante di ricerca (lente d'ingrandimento).
     * <p>
     * Al click, esegue la ricerca e mostra i risultati in un menu popup.
     * </p>
     */
    private void setSearchButton() {

        searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 32, 32);

        searchButton.addActionListener(e -> showPopupMenu(search()));

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, new Insets(5, 5, 5, 0));
        tmpPanel.add(searchButton, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la ricerca degli sviluppatori idonei.
     * <p>
     * Interroga il {@link UserController} cercando utenti (developer o admin) che fanno parte del progetto
     * corrente e la cui email corrisponde al testo inserito.
     * </p>
     *
     * @return Una lista di stringhe contenente le email trovate.
     */
    private List<String> search() {

        boolean success = UserController.getInstance().searchDevOrAdminByEmailAndProject((searchField.getText().equals(SEARCHFIELD_PLACEHOLDER) ?
                "" : searchField.getText()));

        if(!success)
            return new ArrayList<>();

        return UserController.getInstance().getUsersEmails();
    }

    /**
     * Mostra i risultati della ricerca e gestisce l'assegnazione.
     * <p>
     * Crea un {@link JPopupMenu} dinamico con i risultati della ricerca.
     * Per ogni sviluppatore trovato, crea una voce di menu.
     * <br>
     * <b>Azione al click su un risultato:</b>
     * <ol>
     * <li>Invoca {@link IssueController#assignIssueToDeveloper} per salvare l'assegnazione nel DB.</li>
     * <li>Aggiorna l'interfaccia: cambia lo stato a "ASSIGNED" e mostra il nome del developer.</li>
     * <li>Mostra un messaggio di successo.</li>
     * <li>Nasconde il pannello di ricerca (l'assegnazione è completata).</li>
     * </ol>
     * </p>
     *
     * @param developers Lista delle email degli sviluppatori trovati.
     */
    private void showPopupMenu(List<String> developers) {

        JPopupMenu popupMenu = new JPopupMenu();

        for (String developer : developers) {

            JMenuItem item = new JMenuItem(developer);

            item.addActionListener(e -> {

                // Logica di assegnazione effettiva
                boolean success = IssueController.getInstance().assignIssueToDeveloper(developer);

                if(!success)
                    return;

                // Aggiornamento GUI immediato
                statusLabel.setText("Stato: ASSIGNED");
                assignedDeveloperLabel.setText("Developer assegnato: " + developer);
                new FloatingMessage("Assegnazione avvenuta con successo", searchButton, FloatingMessage.SUCCESS_MESSAGE);

                // Nasconde il pannello di assegnazione
                tmpPanel.setVisible(false);
            });

            popupMenu.add(item);
        }

        popupMenu.show(searchButton, 0, searchButton.getHeight());
    }

    /**
     * Configura il campo di testo per l'inserimento dell'email.
     */
    private void setSearchField() {

        searchField = new JTextField(SEARCHFIELD_PLACEHOLDER);

        searchField.setBackground(ColorsList.EMPTY_COLOR);
        searchField.setBorder(BorderFactory.createEmptyBorder());

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchField, SEARCHFIELD_PLACEHOLDER);

        // Permette di cercare anche premendo invio
        searchField.addActionListener(e -> showPopupMenu(search()));

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                192, 0, GridBagConstraints.LINE_START, new Insets(5, 0, 5, 5));
        tmpPanel.add(searchField, Constraints.getGridBagConstraints());
    }
}