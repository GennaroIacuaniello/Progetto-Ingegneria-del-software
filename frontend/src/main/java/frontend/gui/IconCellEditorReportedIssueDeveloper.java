package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;

/**
 * Editor di cella personalizzato per la tabella delle segnalazioni effettuate da uno Sviluppatore.
 * <p>
 * Questa classe estende {@link IconCellEditorReportedIssueUser}. Viene utilizzata quando uno sviluppatore
 * visualizza l'elenco delle issue che ha segnalato lui stesso.
 * La differenza principale rispetto alla classe padre risiede nella finestra di dialogo aperta al click:
 * viene istanziata {@link ShowReportedIssueDeveloper} invece della versione generica utente.
 * </p>
 */
public class IconCellEditorReportedIssueDeveloper extends IconCellEditorReportedIssueUser{

    /**
     * Costruttore dell'editor.
     * <p>
     * Passa i parametri al costruttore della superclasse per configurare l'icona e il pulsante.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param url       Percorso dell'immagine da usare come icona.
     * @param width     Larghezza dell'icona.
     * @param height    Altezza dell'icona.
     * @param table     La tabella di riferimento.
     */
    public IconCellEditorReportedIssueDeveloper(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    /**
     * Gestisce l'azione al click sull'icona.
     * <p>
     * Il flusso operativo è:
     * </p>
     * <ol>
     * <li>Recupera l'issue selezionata dalla riga della tabella.</li>
     * <li>Scarica i dettagli completi tramite {@link IssueController}.</li>
     * <li>Apre la finestra di dialogo {@link ShowReportedIssueDeveloper} per mostrare i dettagli.</li>
     * </ol>
     *
     * @return {@code null}.
     */
    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        boolean success = IssueController.getInstance().getIssueById();

        if(!success)
            return null;

        // Istanzia la dialog specifica per la visualizzazione lato Developer
        ShowReportedIssueUser dialog = new ShowReportedIssueDeveloper(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}