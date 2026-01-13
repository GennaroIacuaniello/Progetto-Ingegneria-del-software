package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

/**
 * Gestore della visualizzazione dei risultati ricerca progetti (Lato Amministratore).
 * <p>
 * Questa classe estende {@link SearchProjectResultsDeveloper} e configura la tabella dei risultati
 * con il set massimo di funzionalità disponibili.
 * Rispetto alla vista sviluppatore, aggiunge due colonne di azione fondamentali per l'amministrazione:
 * </p>
 * <ul>
 * <li><b>View (Vedi tutte le issue):</b> Per monitorare l'intero stato del progetto.</li>
 * <li><b>Teams (Gestisci Team):</b> Per assegnare o rimuovere membri dal progetto.</li>
 * </ul>
 */
public class SearchProjectResultsAdmin extends SearchProjectResultsDeveloper{

    /**
     * Costruttore della classe.
     * <p>
     * Passa i dati alla superclasse per l'inizializzazione di base.
     * </p>
     *
     * @param mainFrame     Il frame principale dell'applicazione.
     * @param homePanel     Il pannello Home (usato per la navigazione al click).
     * @param projectsIds   Lista degli ID dei progetti trovati.
     * @param projectsNames Lista dei nomi dei progetti trovati.
     */
    public SearchProjectResultsAdmin(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectsIds, List<String> projectsNames) {

        super(mainFrame, homePanel, projectsIds, projectsNames);
    }

    /**
     * Definisce la mappa tra i nomi delle azioni e le icone corrispondenti.
     * <p>
     * Aggiunge le icone specifiche per l'admin:
     * </p>
     * <ul>
     * <li>"View" -> {@code viewIssues.png} (Occhio)</li>
     * <li>"Teams" -> {@code teamsIconButton.png} (Gruppo di persone)</li>
     * </ul>
     */
    @Override
    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/frontend/gui/images/reportIssue.png",
                "Reported", "/frontend/gui/images/reportedIssues.png",
                "Assigned", "/frontend/gui/images/assignedIssues.png",
                "View", "/frontend/gui/images/viewIssues.png",
                "Teams", "/frontend/gui/images/teamsIconButton.png"
        );
    }

    /**
     * Definisce l'ordine e l'elenco delle azioni disponibili nelle colonne.
     * <p>
     * L'ordine è: [Report, Reported, Assigned, View, Teams].
     * </p>
     */
    @Override
    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported", "Assigned", "View", "Teams"};
    }

    /**
     * Crea il modello dati per la tabella (Data Model).
     * <p>
     * Costruisce una matrice con 7 colonne:
     * </p>
     * <ol>
     * <li>ID Progetto</li>
     * <li>Nome Progetto</li>
     * <li>Report (Azione)</li>
     * <li>Reported (Azione)</li>
     * <li>Assigned (Azione)</li>
     * <li>View (Azione Admin)</li>
     * <li>Teams (Azione Admin)</li>
     * </ol>
     * <p>
     * Restituisce un'istanza di {@link ProjectTableModelAdmin} che rende editabili (cliccabili) le ultime 5 colonne.
     * </p>
     *
     * @param projectIds   Lista ID.
     * @param projectNames Lista Nomi.
     * @return Il modello configurato.
     */
    @Override
    protected ProjectTableModelUser createTableModel(List<Integer> projectIds, List<String> projectNames) {

        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][7];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";
            rowData[i][3] = "Reported";
            rowData[i][4] = "Assigned";
            rowData[i][5] = "View";
            rowData[i][6] = "Teams";
        }

        return new ProjectTableModelAdmin(rowData);
    }

    /**
     * Costruisce e configura la JTable effettiva.
     * <p>
     * Itera sulle 5 colonne di azione (dalla 2 alla 6) e per ognuna:
     * </p>
     * <ul>
     * <li>Imposta il <b>Renderer</b> per disegnare l'icona corretta.</li>
     * <li>Imposta l'<b>Editor</b> usando {@link ProjectIconCellEditorAdmin}. Questo è cruciale perché
     * collega il click dell'admin alle funzioni di gestione team e supervisione globale.</li>
     * </ul>
     *
     * @param mainFrame    Il frame principale.
     * @param homePanel    Il pannello home.
     * @param projectIds   Dati ID.
     * @param projectNames Dati Nomi.
     * @return La tabella pronta per l'uso.
     */
    @Override
    protected JTable createTable(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectIds, List<String> projectNames) {

        JTable resultsTable = new JTable(createTableModel(projectIds, projectNames));

        // Ciclo per configurare renderer ed editor per le 5 colonne di azione
        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i; // Offset di 2 (ID e Nome occupano 0 e 1)
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
            // Qui viene iniettata la logica specifica dell'Admin
            buttonColumn.setCellEditor(new ProjectIconCellEditorAdmin(mainFrame, homePanel, iconUrl, ICON_WIDTH, ICON_HEIGHT, resultsTable));
        }

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }
}