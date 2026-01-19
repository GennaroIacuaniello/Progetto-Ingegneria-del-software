package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;

/**
 * Gestore per la visualizzazione dei risultati della ricerca issue (Lato Utente).
 * <p>
 * Questa classe ha la responsabilità di costruire e configurare la {@link JTable} contenente
 * i risultati della ricerca.
 * Il suo compito principale è associare ai dati grezzi (titoli delle issue) i componenti grafici corretti:
 * </p>
 * <ul>
 * <li>Il {@link IssueTableModel} per strutturare i dati.</li>
 * <li>Il {@link IconCellRenderer} per visualizzare l'icona "Vedi Dettagli".</li>
 * <li>Il {@link IconCellEditorReportedIssueUser} per gestire il click e aprire la vista di dettaglio utente.</li>
 * </ul>
 * <p>
 * Questa classe viene estesa dalla versione Developer per cambiare il comportamento al click.
 * </p>
 */
public class ReportedIssueSearchResultsPanelUser {

    // Dimensioni standard per l'icona di azione nella tabella
    protected static final int ICON_WIDTH = 20;
    protected static final int ICON_HEIGHT = 20;

    /**
     * Costruttore principale.
     * <p>
     * Al momento dell'istanziazione, genera la tabella dei risultati invocando {@link #createTable}
     * e aggiorna immediatamente la vista nella pagina di ricerca padre tramite
     * {@code searchPage.updateSearchIssueViewResults()}.
     * </p>
     *
     * @param mainFrame    Il frame principale dell'applicazione.
     * @param searchPage   La pagina di ricerca che ospiterà la tabella.
     * @param issuesTitles La lista dei titoli delle issue trovate.
     */
    public ReportedIssueSearchResultsPanelUser(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles, String order) {

        searchPage.updateSearchIssueViewResults(createTable(mainFrame, issuesTitles, order));
    }

    /**
     * Crea e configura la JTable dei risultati.
     * <p>
     * 1. Crea il modello dati tramite {@link #createTableModel}.<br>
     * 2. Configura la colonna 1 (quella del pulsante):
     * </p>
     * <ul>
     * <li>Imposta il Renderer per disegnare l'icona.</li>
     * <li>Imposta l'Editor {@link IconCellEditorReportedIssueUser} per gestire l'apertura
     * della issue in modalità visualizzazione/utente.</li>
     * </ul>
     * <p>
     * 3. Configura l'aspetto grafico (altezza righe, stile header).
     * </p>
     *
     * @param mainFrame    Il frame principale.
     * @param issuesTitles La lista dei dati da visualizzare.
     * @return La tabella configurata pronta per essere mostrata.
     */
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles, String order) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles, order));

        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));
        buttonColumn.setCellEditor(new IconCellEditorReportedIssueUser(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }

    /**
     * Genera il modello dati per la tabella.
     * <p>
     * Converte la lista di stringhe (titoli) in una matrice bidimensionale di oggetti
     * compatibile con {@link IssueTableModel}.
     * <br>
     * Struttura:
     * </p>
     * <ul>
     * <li>Colonna 0: Titolo della issue (dal database).</li>
     * <li>Colonna 1: Stringa "View" (placeholder per il pulsante).</li>
     * </ul>
     *
     * @param issueTitles Lista dei titoli delle issue.
     * @return Il modello dati popolato.
     */
    protected IssueTableModel createTableModel(List<String> issueTitles, String order) {

        int numRows = issueTitles.size();
        Object[][] rowData = new Object[numRows][2];

        if (order.equals("Decrescente")) {

            for (int i = 0; i < numRows; i++) {

                rowData[i][0] = issueTitles.get(i);
                rowData[i][1] = "View"; // Valore fittizio, verrà sovrascritto graficamente dall'icona
            }
        } else {

            for (int i = 0; i < numRows; i++) {

                rowData[i][0] = issueTitles.get(numRows - i - 1);
                rowData[i][1] = "View"; // Valore fittizio, verrà sovrascritto graficamente dall'icona
            }
        }

        return new IssueTableModel(rowData);
    }
}