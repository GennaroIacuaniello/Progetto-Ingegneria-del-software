package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;

/**
 * Pannello per la visualizzazione dei risultati della ricerca globale delle segnalazioni (All Issues).
 * <p>
 * Estende {@link AssignedIssueSearchResultsPanel}, ereditando la struttura base per mostrare una lista
 * di titoli di issue. La differenza principale risiede nella configurazione della tabella:
 * utilizza renderer ed editor specifici ({@link IconCellEditorAllIssue}) che permettono di aprire
 * i dettagli di una qualsiasi issue tramite un'icona dedicata ("occhio").
 * </p>
 */
public class AllIssueSearchResultsPanel extends AssignedIssueSearchResultsPanel {

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
    public AllIssueSearchResultsPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        super(mainFrame, searchPage, issuesTitles);
    }

    /**
     * Crea e configura la tabella per visualizzare i risultati della ricerca.
     * <p>
     * Sovrascrive il metodo della superclasse per utilizzare componenti specifici per la gestione
     * delle issue globali:
     * </p>
     * <ul>
     * <li>Imposta {@link IconCellRenderer} per mostrare l'icona "visualizza" nella colonna delle azioni.</li>
     * <li>Imposta {@link IconCellEditorAllIssue} come editor, gestendo il click sull'icona per aprire i dettagli.</li>
     * <li>Configura l'aspetto grafico dell'header e delle righe.</li>
     * </ul>
     *
     * @param mainFrame    Il frame principale, passato agli editor per gestire i dialoghi.
     * @param issuesTitles La lista dei dati da visualizzare.
     * @return L'istanza di {@link JTable} configurata.
     */
    @Override
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        // Configurazione della colonna delle azioni (indice 1)
        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        // Imposta il renderer per disegnare l'icona
        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));

        // Imposta l'editor per gestire il click sull'icona
        buttonColumn.setCellEditor(new IconCellEditorAllIssue(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        // Configurazione dell'intestazione della tabella
        JTableHeader header = resultsTable.getTableHeader();
        header.setReorderingAllowed(false); // Disabilita il riordinamento delle colonne trascinandole
        header.setResizingAllowed(false);   // Disabilita il ridimensionamento manuale
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }
}