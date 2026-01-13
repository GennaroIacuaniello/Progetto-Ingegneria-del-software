package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;

/**
 * Vista di dettaglio di un'issue specifica per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link ShowReportedIssueUser} ereditando la struttura base della scheda
 * (Titolo, Descrizione, Stato, Tipo, Allegati).
 * <br>
 * La differenza rispetto alla vista utente è l'aggiunta dell'etichetta <b>Priorità</b>.
 * Mentre un utente base vede solo lo stato di avanzamento, lo sviluppatore necessita di sapere
 * quanto è urgente la risoluzione del problema (es. Alta, Critica, Bassa) per organizzare il proprio lavoro.
 * </p>
 */
public class ShowReportedIssueDeveloper extends ShowReportedIssueUser {

    /**
     * Costruttore della vista di dettaglio Developer.
     * <p>
     * Inizializza la finestra richiamando il costruttore della superclasse (che popola i dati comuni)
     * e successivamente aggiunge l'etichetta della priorità al layout.
     * </p>
     *
     * @param parent Il frame genitore (necessario per eventuali dialoghi modali).
     */
    public ShowReportedIssueDeveloper(JFrame parent) {

        super(parent);

        setPriorityLabel();
    }

    /**
     * Recupera e visualizza la priorità dell'issue.
     * <p>
     * 1. Recupera il valore della priorità dal {@link IssueController}.<br>
     * 2. Crea una JLabel formattata.<br>
     * 3. La inserisce in un {@link RoundedPanel} per coerenza stilistica.<br>
     * 4. La posiziona nella griglia del layout (GridBagLayout) accanto agli altri metadati.
     * </p>
     */
    private void setPriorityLabel() {

        JLabel priorityLabel = new JLabel("Priorità: " + IssueController.getInstance().getIssuePriority());
        priorityLabel.setBorder(BorderFactory.createEmptyBorder());
        priorityLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(priorityLabel);

        // Posizionamento nella griglia (riga 1, colonna 3, allineato a destra)
        Constraints.setConstraints(3, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, new Insets(5, 5, 5, 60));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }
}