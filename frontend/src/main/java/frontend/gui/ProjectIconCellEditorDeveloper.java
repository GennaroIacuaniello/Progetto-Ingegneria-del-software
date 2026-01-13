package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

/**
 * Editor di cella per la tabella dei progetti, specifico per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link ProjectIconCellEditorUser} e gestisce la navigazione
 * quando uno sviluppatore clicca sulle icone di azione nella lista dei progetti.
 * Rispetto all'utente base, lo sviluppatore ha accesso a viste specializzate, come la lista
 * delle issue assegnate direttamente a lui ("ISSUE ASSEGNATE").
 * </p>
 */
public class ProjectIconCellEditorDeveloper extends ProjectIconCellEditorUser {

    /**
     * Costruttore dell'editor Developer.
     * <p>
     * Inizializza l'editor richiamando il costruttore della superclasse per configurare
     * l'aspetto grafico e i riferimenti necessari.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home, usato per cambiare la vista centrale.
     * @param url       Il percorso dell'icona.
     * @param width     Larghezza dell'icona.
     * @param height    Altezza dell'icona.
     * @param table     La tabella di riferimento.
     */
    public ProjectIconCellEditorDeveloper(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    /**
     * Gestisce l'azione di navigazione al click sull'icona.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * <ol>
     * <li>Identifica l'azione richiesta tramite il nome della colonna cliccata.</li>
     * <li>Imposta il progetto corrente nel {@link ProjectController} utilizzando ID e Nome presi dalla riga selezionata.</li>
     * <li>Esegue uno switch per determinare quale pannello mostrare:
     * <ul>
     * <li><b>SEGNALA ISSUE:</b> Apre {@link ReportIssueDeveloper} per creare una nuova segnalazione.</li>
     * <li><b>ISSUE SEGNALATE:</b> Apre {@link SearchReportedIssuePageDeveloper} per vedere le issue create dallo sviluppatore.</li>
     * <li><b>ISSUE ASSEGNATE:</b> Apre {@link SearchAssignedIssuePage} per vedere le issue assegnate allo sviluppatore (task list).</li>
     * </ul>
     * </li>
     * </ol>
     * </p>
     *
     * @return {@code null}.
     */
    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        // Imposta il contesto del progetto selezionato nel controller (ID e Nome)
        ProjectController.getInstance().setProjectWithValues((Integer)parentTable.getValueAt(selectedRow, 0),
                (String)parentTable.getValueAt(selectedRow, 1));

        // Router per la navigazione
        switch (action) {

            case "SEGNALA ISSUE":
                homePanel.setContentPanel(new ReportIssueDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                homePanel.setContentPanel(new SearchReportedIssuePageDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE ASSEGNATE":
                homePanel.setContentPanel(new SearchAssignedIssuePage(mainFrame, homePanel));
                break;
        }

        return null;
    }
}