package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;

public class ReportResults {

    private static final String[] columnNames = {"Developers", "Issue Aperte", "Issue Gestite", "Tempo Medio"};

    public ReportResults(TeamReportPage searchPage) {

        searchPage.updateReportViewResults(createTables());
    }

    public ReportResults(DashBoard searchPage) {

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

        for (int i = 0; i < TeamController.getInstance().getDevelopersEmails().size(); i++) {

            developersModel.addRow(new Object[] {

                    TeamController.getInstance().getDevelopersEmails().get(i),
                    TeamController.getInstance().getOpenIssues().get(i),
                    TeamController.getInstance().getResolvedIssues().get(i),
                    formatDuration(TeamController.getInstance().getAverageResolvingDurations().get(i))
            });
        }

        JTable table = new JTable(developersModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setEnabled(false);

        return table;
    }

    private JTable createTotalsTable() {

        DefaultTableModel totalsModel = new DefaultTableModel(columnNames, 0);

        totalsModel.addRow(new Object[] {

                "TOTALE",
                TeamController.getInstance().getTotalOpenIssues(),
                TeamController.getInstance().getTotalResolvedIssues(),
                formatDuration(TeamController.getInstance().getTotalAverageResolvingDuration())
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