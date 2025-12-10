package gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

public class SearchProjectResults {

    //todo:
    /*
        rendi la tabella decente
    */

    private static final Map<String, String> ICON_URL_MAP = Map.of(
            "Report", "/gui/images/reportIssue.png",
            "Reported", "/gui/images/reportedIssues.png",
            "Assigned", "/gui/images/assignedIssues.png",
            "View", "/gui/images/viewIssues.png"
    );

    private static final int ICON_WIDTH = 20;
    private static final int ICON_HEIGHT = 20;

    public SearchProjectResults (List<String > projectsIds, List<String> projectsNames) {

        HomePanel.getInstance().updateSearchProjectViewResults(createTable(projectsIds, projectsNames));
    }

    public JTable createTable(List<String> projectIds, List<String> projectNames) {

        String[] buttonActions = {"Report", "Reported", "Assigned", "View"};
        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][6];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";
            rowData[i][3] = "Reported";
            rowData[i][4] = "Assigned";
            rowData[i][5] = "View";
        }

        ProjectTableModel model = new ProjectTableModel(rowData);
        JTable resultsTable = new JTable(model);

        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i;
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
            buttonColumn.setCellEditor(new IconCellEditor(iconUrl, ICON_WIDTH, ICON_HEIGHT, resultsTable));
        }

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        resultsTable.getTableHeader().setReorderingAllowed(false);

        return resultsTable;
    }
}
