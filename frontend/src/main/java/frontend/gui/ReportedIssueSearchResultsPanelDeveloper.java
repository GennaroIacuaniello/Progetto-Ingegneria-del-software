package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;

/**
 * Pannello per la visualizzazione dei risultati di ricerca (segnalazioni) per gli Sviluppatori.
 * <p>
 * Questa classe estende {@link ReportedIssueSearchResultsPanelUser} ereditando la struttura di base
 * (layout, titolo, pulsante indietro).
 * La specializzazione avviene nel metodo {@link #createTable}, dove viene iniettato un comportamento
 * differente per la colonna delle azioni: cliccando sull'icona "Vedi Issue", lo sviluppatore
 * accederà a funzionalità avanzate (gestite da {@link IconCellEditorReportedIssueDeveloper}).
 * </p>
 */
public class ReportedIssueSearchResultsPanelDeveloper extends ReportedIssueSearchResultsPanelUser{

    /**
     * Costruttore del pannello dei risultati Developer.
     * <p>
     * Passa i parametri alla superclasse per l'inizializzazione standard.
     * </p>
     *
     * @param mainFrame    Il frame principale dell'applicazione.
     * @param searchPage   La pagina di ricerca padre (per la navigazione indietro).
     * @param issuesTitles La lista dei titoli delle issue trovate da visualizzare.
     */
    public ReportedIssueSearchResultsPanelDeveloper(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        super(mainFrame, searchPage, issuesTitles);
    }

    /**
     * Crea e configura la tabella dei risultati specifica per lo sviluppatore.
     * <p>
     * Configura la JTable con le seguenti caratteristiche:
     * </p>
     * <ul>
     * <li>Utilizza il renderer grafico standard (icona "showIssueIconButton").</li>
     * <li><b>Differenza Chiave:</b> Imposta {@link IconCellEditorReportedIssueDeveloper} come editor della cella.
     * Questo garantisce che al click venga aperta la vista di dettaglio per sviluppatori/admin
     * invece di quella in sola lettura per utenti.</li>
     * <li>Imposta l'altezza delle righe e lo stile dell'header (non ridimensionabile, colore specifico).</li>
     * </ul>
     *
     * @param mainFrame    Il frame principale.
     * @param issuesTitles La lista dei titoli per popolare il modello dati.
     * @return La {@link JTable} configurata.
     */
    @Override
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        // Configurazione della colonna azioni (Indice 1)
        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        // Renderer: Disegna l'icona (Aspetto visivo uguale all'utente)
        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));

        // Editor: Gestisce il click (Comportamento specifico per Developer)
        buttonColumn.setCellEditor(new IconCellEditorReportedIssueDeveloper(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }
}