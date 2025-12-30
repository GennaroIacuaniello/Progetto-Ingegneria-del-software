package frontend.gui;

import frontend.controller.ControllerTMP;

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
        tablesPanel.setBackground(ColorsList.EMPTY_COLOR);

        JTable developersTable = createDevelopersTable();

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.PAGE_END);
        tablesPanel.add(developersTable, Constraints.getGridBagConstraints());

        JTable totalTable = createTotalsTable();
        totalTable.setColumnModel(developersTable.getColumnModel());

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.PAGE_START);
        tablesPanel.add(totalTable, Constraints.getGridBagConstraints());

        return tablesPanel;
    }

    private JTable createDevelopersTable() {

        DefaultTableModel developersModel = new DefaultTableModel(columnNames, 0);

        for (int i = 0; i < ControllerTMP.getDevelopers().size(); i++) {

            developersModel.addRow(new Object[] {

                    ControllerTMP.getDevelopers().get(i),
                    ControllerTMP.getOpenIssues().get(i),
                    ControllerTMP.getResolvedIssues().get(i),
                    formatDuration(ControllerTMP.getAverageResolvingDurations().get(i))
            });
        }

        JTable table = new JTable(developersModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setEnabled(false);

        return table;
    }

    private JTable createTotalsTable() {

        DefaultTableModel totalsModel = new DefaultTableModel(columnNames, 0);

        totalsModel.addRow(new Object[] {

                "TOTALE",
                ControllerTMP.getTotalOpenIssues(),
                ControllerTMP.getTotalResolvedIssues(),
                formatDuration(ControllerTMP.getTotalAverageResolvingDuration())
        });

        JTable table = new JTable(totalsModel);
        table.setTableHeader(null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setBackground(Color.LIGHT_GRAY);
        table.setEnabled(false);

        return table;
    }

    private String formatDuration(Duration d) {
        if (d == null) return "0g 0h";
        return String.format("%dg %dh", d.toDays(), d.toHoursPart());
    }
}