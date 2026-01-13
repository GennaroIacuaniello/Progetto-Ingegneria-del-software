package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

/**
 * Editor di cella per la tabella dei progetti, specifico per il ruolo Amministratore.
 * <p>
 * Questa classe estende {@link ProjectIconCellEditorDeveloper} e gestisce gli eventi di click
 * sulle icone di azione nella lista dei progetti.
 * Agisce come un "router" di navigazione: quando un amministratore clicca su un'icona (es. "Gestisci Team" o "Vedi tutte le issue"),
 * questa classe imposta il progetto corrente nel controller e sostituisce il pannello centrale della Home
 * con la schermata richiesta.
 * </p>
 */
public class ProjectIconCellEditorAdmin extends ProjectIconCellEditorDeveloper {

    /**
     * Costruttore dell'editor Admin.
     * <p>
     * Passa tutti i parametri alla superclasse per la configurazione dell'aspetto grafico (icona) e
     * dei riferimenti ai componenti principali.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home utente, necessario per cambiare le viste (setContentPanel).
     * @param url       Il percorso dell'icona da visualizzare.
     * @param width     Larghezza dell'icona.
     * @param height    Altezza dell'icona.
     * @param table     La tabella di riferimento.
     */
    public ProjectIconCellEditorAdmin(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    /**
     * Gestisce l'azione eseguita al click sull'icona.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ol>
     * <li>Identifica l'azione richiesta leggendo il nome della colonna cliccata.</li>
     * <li>Recupera l'ID del progetto dalla prima colonna della riga selezionata e lo imposta come attivo nel {@link ProjectController}.</li>
     * <li>Utilizza uno switch sul nome della colonna per determinare quale nuova vista aprire nel pannello centrale:
     * <ul>
     * <li><b>SEGNALA ISSUE:</b> Apre la schermata di segnalazione.</li>
     * <li><b>ISSUE SEGNALATE:</b> Apre la lista delle issue segnalate dall'admin corrente.</li>
     * <li><b>ISSUE ASSEGNATE:</b> Apre la lista delle issue assegnate all'admin.</li>
     * <li><b>VEDI TUTTE LE ISSUE:</b> Apre la vista globale delle issue (funzionalità di supervisione).</li>
     * <li><b>GESTISCI TEAM:</b> Apre il pannello di gestione dei team e dei membri (funzionalità amministrativa).</li>
     * </ul>
     * </li>
     * </ol>
     * </p>
     *
     * @return {@code null}, poiché l'azione è la navigazione e non la modifica del valore della cella.
     */
    @Override
    public Object getCellEditorValue() {

        // Identifica l'azione basandosi sull'intestazione della colonna
        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        // Imposta il contesto del progetto nel controller
        ProjectController.getInstance().setProjectWithId((Integer)parentTable.getValueAt(selectedRow, 0));

        // Navigazione verso la schermata appropriata
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

            case "VEDI TUTTE LE ISSUE":
                homePanel.setContentPanel(new SearchAllIssuePage(mainFrame, homePanel));
                break;

            case "GESTISCI TEAM":
                homePanel.setContentPanel(new ManageTeamsPanel(mainFrame, homePanel));
                break;
        }

        return null;
    }
}