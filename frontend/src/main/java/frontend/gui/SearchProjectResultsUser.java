package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

/**
 * Gestore della visualizzazione dei risultati ricerca progetti (Lato Utente Standard).
 * <p>
 * Questa classe ha la responsabilità di trasformare i dati grezzi dei progetti (ID e Nomi)
 * in una tabella interattiva ({@link JTable}) integrata nella GUI.
 * <br>
 * Funge da "View Builder" e da classe base per le specializzazioni di ruolo:
 * </p>
 * <ul>
 * <li>Definisce quali azioni sono disponibili (per l'utente base: "Report" e "Reported").</li>
 * <li>Associa le icone alle azioni.</li>
 * <li>Costruisce il {@link javax.swing.table.TableModel}.</li>
 * <li>Configura i Renderer (immagini) e gli Editor (click) per le colonne pulsante.</li>
 * </ul>
 */
public class SearchProjectResultsUser {

    // Configurazioni per le azioni e le icone
    protected static String[] buttonActions;
    protected static Map<String, String> ICON_URL_MAP;

    // Dimensioni standard per le icone nella tabella
    protected static final int ICON_WIDTH = 20;
    protected static final int ICON_HEIGHT = 20;

    /**
     * Costruttore principale.
     * <p>
     * Esegue la sequenza di inizializzazione:
     * </p>
     * <ol>
     * <li>Configura la mappa delle icone e le azioni disponibili (sovrascrivibili dalle sottoclassi).</li>
     * <li>Costruisce la tabella completa chiamando {@link #createTable}.</li>
     * <li><b>Aggiorna la vista:</b> Invia immediatamente la tabella generata al pannello Home tramite
     * {@code homePanel.updateSearchProjectViewResults()}.</li>
     * </ol>
     *
     * @param mainFrame     Il frame principale.
     * @param homePanel     Il pannello Home che ospiterà i risultati.
     * @param projectsIds   Lista degli ID dei progetti trovati.
     * @param projectsNames Lista dei nomi dei progetti trovati.
     */
    public SearchProjectResultsUser (JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectsIds, List<String> projectsNames) {

        setIconUrlMap();
        setButtonActions();

        homePanel.updateSearchProjectViewResults(createTable(mainFrame, homePanel, projectsIds, projectsNames));
    }

    /**
     * Inizializza la mappa che associa i nomi logici delle azioni ai percorsi delle icone.
     * <p>
     * Per l'utente base:
     * </p>
     * <ul>
     * <li>"Report" -> Icona per segnalare una issue.</li>
     * <li>"Reported" -> Icona per vedere le issue segnalate.</li>
     * </ul>
     * <p>
     * Questo metodo è {@code protected} per essere esteso/sovrascritto da Developer e Admin.
     * </p>
     */
    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/frontend/gui/images/reportIssue.png",
                "Reported", "/frontend/gui/images/reportedIssues.png"
        );
    }

    /**
     * Definisce l'ordine e l'elenco delle colonne di azione.
     */
    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported"};
    }

    /**
     * Crea e configura la JTable dei risultati.
     * <p>
     * Questo è il metodo core della classe:
     * </p>
     * <ol>
     * <li>Crea il modello dati tramite {@link #createTableModel}.</li>
     * <li>Itera sull'array {@code buttonActions} per configurare le colonne dinamiche:
     * <ul>
     * <li>Calcola l'indice della colonna (partendo da 2, dato che 0 e 1 sono ID e Nome).</li>
     * <li>Imposta {@link IconCellRenderer} per mostrare l'immagine.</li>
     * <li>Imposta {@link ProjectIconCellEditorUser} per gestire i click.</li>
     * </ul>
     * </li>
     * <li>Configura l'aspetto estetico (altezza righe, header non riordinabile).</li>
     * </ol>
     *
     * @return La tabella configurata pronta per essere visualizzata.
     */
    protected JTable createTable(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectIds, List<String> projectNames) {

        JTable resultsTable = new JTable(createTableModel(projectIds, projectNames));

        // Configurazione dinamica delle colonne pulsante
        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i; // Offset di 2 per saltare ID e NOME
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
            // Qui viene usato l'Editor base per Utente
            buttonColumn.setCellEditor(new ProjectIconCellEditorUser(mainFrame, homePanel, iconUrl, ICON_WIDTH, ICON_HEIGHT, resultsTable));
        }

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }

    /**
     * Genera il modello dati (TableModel).
     * <p>
     * Costruisce una matrice di oggetti per popolare la tabella.
     * <br>
     * Struttura base (4 colonne):
     * </p>
     * <ol>
     * <li><b>0:</b> ID Progetto.</li>
     * <li><b>1:</b> Nome Progetto.</li>
     * <li><b>2:</b> Stringa "Report" (chiave per l'azione).</li>
     * <li><b>3:</b> Stringa "Reported" (chiave per l'azione).</li>
     * </ol>
     * <p>
     * Restituisce {@link ProjectTableModelUser} che definisce quali celle sono editabili.
     * </p>
     */
    protected ProjectTableModelUser createTableModel(List<Integer> projectIds, List<String> projectNames) {

        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][4];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";   // Placeholder per il bottone
            rowData[i][3] = "Reported"; // Placeholder per il bottone
        }

        return new ProjectTableModelUser(rowData);
    }
}