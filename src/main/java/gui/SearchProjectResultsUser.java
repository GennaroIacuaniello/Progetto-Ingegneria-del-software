package gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

public class SearchProjectResultsUser {

    protected static String[] buttonActions;
    protected static Map<String, String> ICON_URL_MAP;
    protected static final int ICON_WIDTH = 20;
    protected static final int ICON_HEIGHT = 20;

    public SearchProjectResultsUser (JFrame mainFrame, HomePanelUser homePanel, List<String > projectsIds, List<String> projectsNames) {

        setIconUrlMap();
        setButtonActions();

        homePanel.updateSearchProjectViewResults(createTable(mainFrame, homePanel, projectsIds, projectsNames));
    }

    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/gui/images/reportIssue.png",
                "Reported", "/gui/images/reportedIssues.png"
        );
    }

    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported"};
    }

    protected JTable createTable(JFrame mainFrame, HomePanelUser homePanel, List<String> projectIds, List<String> projectNames) {

        JTable resultsTable = new JTable(createTableModel(projectIds, projectNames));

        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i;
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
            buttonColumn.setCellEditor(new IconCellEditorUser(mainFrame, homePanel, iconUrl, ICON_WIDTH, ICON_HEIGHT, resultsTable));
        }

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        resultsTable.getTableHeader().setReorderingAllowed(false);

        return resultsTable;
    }

    protected ProjectTableModelUser createTableModel(List<String> projectIds, List<String> projectNames) {

        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][4];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";
            rowData[i][3] = "Reported";
        }

        return new ProjectTableModelUser(rowData);
    }
}
