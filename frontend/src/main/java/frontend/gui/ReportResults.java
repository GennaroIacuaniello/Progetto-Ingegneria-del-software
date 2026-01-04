package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.Duration;
import java.util.List;

public class ReportResults {

    private final List<String> developers;
    private final List<Integer> openIssues;
    private final List<Integer> resolvedIssues;
    private final List<Duration> averageResolvingDurations;
    private final int totalOpenIssues;
    private final int totalResolvedIssues;
    private final Duration totalAverageResolvingDuration;

    private static final String[] columnNames = {"Developers", "Issue Aperte", "Issue Gestite", "Tempo Medio"};

    public ReportResults(TeamReportPage searchPage) {

        developers = TeamController.getInstance().getDevelopersEmails();
        openIssues = TeamController.getInstance().getOpenIssues();
        resolvedIssues = TeamController.getInstance().getResolvedIssues();
        averageResolvingDurations = TeamController.getInstance().getAverageResolvingDurations();

        totalOpenIssues = TeamController.getInstance().getTotalOpenIssues();
        totalResolvedIssues = TeamController.getInstance().getTotalResolvedIssues();
        totalAverageResolvingDuration = TeamController.getInstance().getTotalAverageResolvingDuration();

        searchPage.updateReportViewResults(createTables());
    }

    public ReportResults(DashBoard searchPage) {

        developers = ProjectController.getInstance().getDevelopersEmails();
        openIssues = ProjectController.getInstance().getOpenIssues();
        resolvedIssues = ProjectController.getInstance().getResolvedIssues();
        averageResolvingDurations = ProjectController.getInstance().getAverageResolvingDurations();

        totalOpenIssues = ProjectController.getInstance().getTotalOpenIssues();
        totalResolvedIssues = ProjectController.getInstance().getTotalResolvedIssues();
        totalAverageResolvingDuration = ProjectController.getInstance().getTotalAverageResolvingDuration();

        searchPage.setDashBoardViewResults(createTables());
    }

    private Component createTables() {

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new GridBagLayout());
        tablesPanel.setBorder(BorderFactory.createEmptyBorder());
        tablesPanel.setOpaque(true);
        tablesPanel.setBackground(Color.WHITE);

        JTable developersTable = createDevelopersTable();

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.PAGE_START, 1f, 0.01f);
        tablesPanel.add(developersTable.getTableHeader(), Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1f, 1f);
        tablesPanel.add(developersTable, Constraints.getGridBagConstraints());

        JTable totalTable = createTotalsTable();
        totalTable.setColumnModel(developersTable.getColumnModel());

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.PAGE_END, 1f, 0.01f);
        tablesPanel.add(totalTable, Constraints.getGridBagConstraints());

        return tablesPanel;
    }

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

    private JTable createTotalsTable() {

        DefaultTableModel totalsModel = new DefaultTableModel(columnNames, 0);

        totalsModel.addRow(new Object[] {

                "TOTALE",
                totalOpenIssues,
                totalResolvedIssues,
                formatDuration(totalAverageResolvingDuration),
        });

        JTable table = new JTable(totalsModel);
        table.setTableHeader(null);
        table.setBackground(Color.LIGHT_GRAY);
        table.setEnabled(false);

        return table;
    }

    private String formatDuration(Duration d) {
        if (d == null) return "0g 0h";
        return String.format("%dg %dh", d.toDays(), d.toHoursPart());
    }
}