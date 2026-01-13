package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;

/**
 * Pannello per la visualizzazione dei risultati della ricerca delle segnalazioni assegnate (Assigned Issues).
 * <p>
 * Questa classe estende {@link ReportedIssueSearchResultsPanelDeveloper} per mostrare l'elenco delle issue
 * assegnate allo sviluppatore corrente. La differenza principale rispetto alla classe padre
 * risiede nella configurazione della tabella: utilizza un renderer e un editor personalizzati
 * ({@link IconCellEditorAssignedIssue}) che, oltre a visualizzare i dettagli, permettono
 * allo sviluppatore di modificare lo stato della issue (es. segnarla come risolta).
 * </p>
 */
public class AssignedIssueSearchResultsPanel extends ReportedIssueSearchResultsPanelDeveloper {

    /**
     * Costruttore del pannello dei risultati.
     * <p>
     * Chiama il costruttore della superclasse per inizializzare il layout e popolare la lista.
     * </p>
     *
     * @param mainFrame    Il frame principale dell'applicazione.
     * @param searchPage   La pagina di ricerca che ospita questo pannello.
     * @param issuesTitles La lista dei titoli delle issue trovate dalla ricerca.
     */
    public AssignedIssueSearchResultsPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        super(mainFrame, searchPage, issuesTitles);
    }

    /**
     * Crea e configura la tabella per visualizzare i risultati della ricerca.
     * <p>
     * Sovrascrive il metodo della superclasse per utilizzare componenti specifici per le issue assegnate:
     * <ul>
     * <li>Imposta {@link IconCellRenderer} per mostrare l'icona "visualizza" (occhio) nella colonna delle azioni.</li>
     * <li>Imposta {@link IconCellEditorAssignedIssue} come editor, che gestisce il click sull'icona aprendo la finestra di dettaglio con permessi di modifica.</li>
     * <li>Configura l'aspetto grafico dell'header e l'altezza delle righe.</li>
     * </ul>
     * </p>
     *
     * @param mainFrame    Il frame principale.
     * @param issuesTitles La lista dei titoli da visualizzare.
     * @return L'istanza di {@link JTable} configurata.
     */
    @Override
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        // Configurazione della colonna delle azioni (indice 1)
        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        // Imposta il renderer per visualizzare l'icona
        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));

        // Imposta l'editor specifico per le issue assegnate
        buttonColumn.setCellEditor(new IconCellEditorAssignedIssue(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        // Configurazione dell'intestazione della tabella
        JTableHeader header = resultsTable.getTableHeader();
        header.setReorderingAllowed(false); // Disabilita il riordinamento delle colonne
        header.setResizingAllowed(false);   // Disabilita il ridimensionamento delle colonne
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }
}