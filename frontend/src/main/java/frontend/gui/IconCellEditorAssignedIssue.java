package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;

/**
 * Editor di cella personalizzato per la tabella delle issue assegnate (Assigned Issues).
 * <p>
 * Questa classe estende {@link IconCellEditorReportedIssueDeveloper} e gestisce l'interazione
 * con l'icona "visualizza/modifica" presente nella tabella delle segnalazioni assegnate allo sviluppatore corrente.
 * Al click, viene aperta la finestra di dialogo specifica {@link ShowAssignedIssue}, che consente
 * non solo di visualizzare i dettagli, ma anche di avanzare lo stato della segnalazione (workflow).
 * </p>
 */
public class IconCellEditorAssignedIssue extends IconCellEditorReportedIssueDeveloper {

    /**
     * Costruttore dell'editor.
     * <p>
     * Inizializza l'editor richiamando il costruttore della superclasse per configurare il pulsante e l'icona.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param url       Il percorso dell'icona da visualizzare nel pulsante.
     * @param width     Larghezza dell'icona.
     * @param height    Altezza dell'icona.
     * @param table     La tabella di riferimento.
     */
    public IconCellEditorAssignedIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    /**
     * Esegue l'azione associata al click sulla cella.
     * <p>
     * La logica è la seguente:
     * <ol>
     * <li>Individua l'issue corrispondente alla riga selezionata nella tabella.</li>
     * <li>Recupera i dettagli completi dell'issue tramite {@link IssueController#getIssueById()}.</li>
     * <li>Se il recupero ha successo, apre la finestra di dialogo {@link ShowAssignedIssue},
     * specifica per le issue in carico all'utente, permettendo azioni di modifica stato.</li>
     * </ol>
     * </p>
     *
     * @return {@code null}, in quanto l'azione principale è l'apertura del dialog e non la modifica del valore della cella nel modello.
     */
    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        boolean success = IssueController.getInstance().getIssueById();

        if(!success)
            return null;

        ShowReportedIssueUser dialog = new ShowAssignedIssue(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}