package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.Duration;
import java.util.List;

/**
 * Classe responsabile della generazione e visualizzazione dei report statistici.
 * <p>
 * Questa classe agisce come un ponte tra i dati statistici (forniti dai Controller) e l'interfaccia grafica.
 * È in grado di generare visualizzazioni tabellari per due contesti diversi:
 * <ol>
 * <li><b>Report di Team:</b> Statistiche specifiche per i membri di un singolo team (via {@link TeamController}).</li>
 * <li><b>Dashboard di Progetto:</b> Statistiche globali per tutti gli sviluppatori del progetto (via {@link ProjectController}).</li>
 * </ol>
 * La visualizzazione prodotta include una tabella di dettaglio per ogni sviluppatore e una riga di riepilogo con i totali.
 * </p>
 */
public class ReportResults {

    // Liste parallele contenenti i dati statistici
    private final List<String> developers;
    private final List<Integer> openIssues;
    private final List<Integer> resolvedIssues;
    private final List<Duration> averageResolvingDurations;

    // Variabili per i totali aggregati
    private final int totalOpenIssues;
    private final int totalResolvedIssues;
    private final Duration totalAverageResolvingDuration;

    private static final String[] columnNames = {"Developers", "Issue Aperte", "Issue Gestite", "Tempo Medio"};

    /**
     * Costruttore per la generazione del Report di un Team.
     * <p>
     * Recupera i dati tramite {@link TeamController} (specifici per il team selezionato)
     * e aggiorna immediatamente la vista {@link TeamReportPage} con le tabelle generate.
     * </p>
     *
     * @param searchPage La pagina del report del team che richiede i dati.
     */
    public ReportResults(TeamReportPage searchPage) {

        developers = TeamController.getInstance().getDevelopersEmails();
        openIssues = TeamController.getInstance().getOpenIssues();
        resolvedIssues = TeamController.getInstance().getResolvedIssues();
        averageResolvingDurations = TeamController.getInstance().getAverageResolvingDurations();

        totalOpenIssues = TeamController.getInstance().getTotalOpenIssues();
        totalResolvedIssues = TeamController.getInstance().getTotalResolvedIssues();
        totalAverageResolvingDuration = TeamController.getInstance().getTotalAverageResolvingDuration();

        // Inietta il componente creato nella pagina chiamante
        searchPage.updateReportViewResults(createTables());
    }

    /**
     * Costruttore per la generazione della Dashboard di Progetto.
     * <p>
     * Recupera i dati tramite {@link ProjectController} (statistiche globali del progetto)
     * e aggiorna immediatamente la vista {@link DashBoard} con le tabelle generate.
     * </p>
     *
     * @param searchPage La dashboard che richiede i dati.
     */
    public ReportResults(DashBoard searchPage) {

        developers = ProjectController.getInstance().getDevelopersEmails();
        openIssues = ProjectController.getInstance().getOpenIssues();
        resolvedIssues = ProjectController.getInstance().getResolvedIssues();
        averageResolvingDurations = ProjectController.getInstance().getAverageResolvingDurations();

        totalOpenIssues = ProjectController.getInstance().getTotalOpenIssues();
        totalResolvedIssues = ProjectController.getInstance().getTotalResolvedIssues();
        totalAverageResolvingDuration = ProjectController.getInstance().getTotalAverageResolvingDuration();

        // Inietta il componente creato nella dashboard
        searchPage.setDashBoardViewResults(createTables());
    }

    /**
     * Metodo principale per la costruzione dell'interfaccia grafica del report.
     * <p>
     * Crea un pannello contenitore che ospita due tabelle distinte ma visivamente allineate:
     * <ol>
     * <li><b>Tabella Sviluppatori:</b> Contiene il dettaglio riga per riga.</li>
     * <li><b>Tabella Totali:</b> Una tabella a riga singola in basso per i dati aggregati.</li>
     * </ol>
     * </p>
     *
     * @return Il componente {@link Component} (JPanel) contenente le tabelle assemblate.
     */
    private Component createTables() {

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new GridBagLayout());
        tablesPanel.setBorder(BorderFactory.createEmptyBorder());
        tablesPanel.setOpaque(true);
        tablesPanel.setBackground(Color.WHITE);

        // 1. Creazione Tabella Dettagli
        JTable developersTable = createDevelopersTable();

        // Aggiunta Header
        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.PAGE_START, 1f, 0.01f);
        tablesPanel.add(developersTable.getTableHeader(), Constraints.getGridBagConstraints());

        // Aggiunta Corpo Tabella
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1f, 1f);
        tablesPanel.add(developersTable, Constraints.getGridBagConstraints());

        // 2. Creazione Tabella Totali
        JTable totalTable = createTotalsTable();

        // *Trucco Swing*: Condivide il ColumnModel per garantire che le colonne
        // della tabella totali siano larghe esattamente quanto quelle della tabella dettagli.
        totalTable.setColumnModel(developersTable.getColumnModel());

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.PAGE_END, 1f, 0.01f);
        tablesPanel.add(totalTable, Constraints.getGridBagConstraints());

        return tablesPanel;
    }

    /**
     * Crea la tabella con i dettagli per ogni sviluppatore.
     * <p>
     * Itera sulle liste di dati (Email, Issue Aperte, Risolte, Durata) e popola il modello.
     * La tabella è impostata come non modificabile (enabled = false).
     * </p>
     */
    private JTable createDevelopersTable() {

        DefaultTableModel developersModel = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < developers.size(); i++) {

            developersModel.addRow(new Object[] {
                    developers.get(i),
                    openIssues.get(i),
                    resolvedIssues.get(i),
                    formatDuration(averageResolvingDurations.get(i))
            });
        }

        JTable table = new JTable(developersModel);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        table.setEnabled(false);

        return table;
    }

    /**
     * Crea la tabella di riepilogo (Footer).
     * <p>
     * Contiene una sola riga con i totali calcolati.
     * Ha uno sfondo grigio chiaro per distinguersi visivamente dai dati di dettaglio.
     * </p>
     */
    private JTable createTotalsTable() {

        DefaultTableModel totalsModel = new DefaultTableModel(columnNames, 0);

        totalsModel.addRow(new Object[] {
                "TOTALE",
                totalOpenIssues,
                totalResolvedIssues,
                formatDuration(totalAverageResolvingDuration),
        });

        JTable table = new JTable(totalsModel);
        table.setTableHeader(null); // Nasconde l'header (usa quello della tabella sopra)
        table.setBackground(Color.LIGHT_GRAY);
        table.setEnabled(false);

        return table;
    }

    /**
     * Formatta un oggetto Duration in una stringa leggibile.
     *
     * @param d L'oggetto Duration.
     * @return Stringa nel formato "Xg Yh" (Giorni e Ore), o "0g 0h" se null.
     */
    private String formatDuration(Duration d) {
        if (d == null) return "0g 0h";
        return String.format("%dg %dh", d.toDays(), d.toHoursPart());
    }
}