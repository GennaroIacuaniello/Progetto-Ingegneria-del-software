package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;

/**
 * Editor di cella personalizzato per la tabella dei risultati della ricerca globale (All Issues).
 * <p>
 * Questa classe estende {@link IconCellEditorAssignedIssue}, ereditando la gestione del pulsante grafico.
 * Tuttavia, sovrascrive la logica di azione (il metodo {@code getCellEditorValue}): quando l'utente clicca sull'icona,
 * viene recuperata l'issue corrispondente e visualizzata tramite la finestra di dialogo {@link ShowIssueAdmin}.
 * Questo permette una visualizzazione dettagliata adatta al contesto di amministrazione o supervisione globale.
 * </p>
 */
public class IconCellEditorAllIssue extends IconCellEditorAssignedIssue{

    /**
     * Costruttore dell'editor.
     * <p>
     * Chiama il costruttore della superclasse per configurare il pulsante e i riferimenti alla tabella.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param url       Il percorso dell'immagine icona da visualizzare.
     * @param width     La larghezza dell'icona.
     * @param height    L'altezza dell'icona.
     * @param table     La tabella JTable a cui questo editor appartiene.
     */
    public IconCellEditorAllIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    /**
     * Definisce l'azione da eseguire quando l'utente attiva l'editor (clicca sull'icona).
     * <p>
     * Esegue i seguenti passaggi:
     * </p>
     * <ol>
     * <li>Identifica la riga selezionata nella tabella.</li>
     * <li>Comunica al {@link IssueController} quale issue è stata selezionata.</li>
     * <li>Richiede al controller di scaricare i dettagli completi dell'issue ({@code getIssueById}).</li>
     * <li>In caso di successo, istanzia e mostra la finestra di dialogo {@link ShowIssueAdmin} per visualizzare i dettagli.</li>
     * </ol>
     *
     * @return {@code null}, poiché l'editor non deve restituire un valore da salvare nel modello della tabella, ma solo innescare l'apertura del dialog.
     */
    @Override
    public Object getCellEditorValue() {

        // Imposta l'issue corrente nel controller basandosi sull'indice della riga selezionata
        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));

        // Recupera i dettagli completi dal backend
        boolean success = IssueController.getInstance().getIssueById();

        if(!success)
            return null;

        // Apre la finestra di visualizzazione specifica per la vista "Admin/All Issues"
        ShowReportedIssueUser dialog = new ShowIssueAdmin(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}