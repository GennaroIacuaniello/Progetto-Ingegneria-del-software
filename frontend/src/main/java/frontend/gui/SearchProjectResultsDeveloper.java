package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

/**
 * Gestore della visualizzazione dei risultati ricerca progetti (Lato Sviluppatore).
 * <p>
 * Questa classe estende {@link SearchProjectResultsUser} e arricchisce la tabella dei risultati.
 * Mantiene le funzionalità dell'utente standard (Segnalazione e visualizzazione delle proprie segnalazioni),
 * ma aggiunge la colonna "Assigned" (Issue Assegnate), permettendo allo sviluppatore di accedere
 * rapidamente ai task di cui è responsabile.
 * </p>
 */
public class SearchProjectResultsDeveloper extends SearchProjectResultsUser{

    /**
     * Costruttore della classe.
     * <p>
     * Inizializza il componente passando i dati alla superclasse.
     * </p>
     *
     * @param mainFrame     Il frame principale dell'applicazione.
     * @param homePanel     Il pannello Home (usato per il cambio schermata).
     * @param projectsIds   Lista degli ID dei progetti trovati.
     * @param projectsNames Lista dei nomi dei progetti trovati.
     */
    public SearchProjectResultsDeveloper(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectsIds, List<String> projectsNames) {

        super(mainFrame, homePanel, projectsIds, projectsNames);
    }

    /**
     * Definisce la mappa tra i nomi delle azioni e le icone corrispondenti.
     * <p>
     * Sovrascrive il metodo base per includere l'icona per le issue assegnate:
     * <ul>
     * <li>"Report" -> Icona di segnalazione.</li>
     * <li>"Reported" -> Icona lista segnalazioni fatte.</li>
     * <li><b>"Assigned"</b> -> Icona lista task assegnati (Specifica Developer).</li>
     * </ul>
     * </p>
     */
    @Override
    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/frontend/gui/images/reportIssue.png",
                "Reported", "/frontend/gui/images/reportedIssues.png",
                "Assigned", "/frontend/gui/images/assignedIssues.png"
        );
    }

    /**
     * Definisce l'ordine e l'elenco delle azioni disponibili nelle colonne.
     * <p>
     * L'ordine delle colonne attive sarà: [Report, Reported, Assigned].
     * </p>
     */
    @Override
    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported", "Assigned"};
    }

    /**
     * Crea il modello dati per la tabella (Data Model).
     * <p>
     * Costruisce una matrice con 5 colonne:
     * <ol>
     * <li>ID Progetto</li>
     * <li>Nome Progetto</li>
     * <li>Report (Azione)</li>
     * <li>Reported (Azione)</li>
     * <li>Assigned (Azione)</li>
     * </ol>
     * Restituisce un'istanza di {@link ProjectTableModelDeveloper} che rende editabili le ultime 3 colonne.
     * </p>
     *
     * @param projectIds   Lista ID.
     * @param projectNames Lista Nomi.
     * @return Il modello configurato.
     */
    @Override
    protected ProjectTableModelUser createTableModel(List<Integer> projectIds, List<String> projectNames) {

        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][5]; // 5 colonne totali

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";
            rowData[i][3] = "Reported";
            rowData[i][4] = "Assigned";
        }

        return new ProjectTableModelDeveloper(rowData);
    }

    /**
     * Costruisce e configura la JTable effettiva.
     * <p>
     * Itera sulle 3 colonne di azione (dalla 2 alla 4) e per ognuna:
     * <ul>
     * <li>Imposta il <b>Renderer</b> per l'icona.</li>
     * <li>Imposta l'<b>Editor</b> usando {@link ProjectIconCellEditorDeveloper}. Questo garantisce che
     * il click sull'icona "Assigned" porti effettivamente alla pagina dei task assegnati.</li>
     * </ul>
     * </p>
     *
     * @param mainFrame    Il frame principale.
     * @param homePanel    Il pannello home.
     * @param projectIds   Dati ID.
     * @param projectNames Dati Nomi.
     * @return La tabella configurata.
     */
    @Override
    protected JTable createTable(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectIds, List<String> projectNames) {

        JTable resultsTable = new JTable(createTableModel(projectIds, projectNames));

        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i; // Offset di 2 colonne (ID e Nome)
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
            // Configura l'editor specifico per Developer
            buttonColumn.setCellEditor(new ProjectIconCellEditorDeveloper(mainFrame, homePanel, iconUrl, ICON_WIDTH, ICON_HEIGHT, resultsTable));
        }

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }
}