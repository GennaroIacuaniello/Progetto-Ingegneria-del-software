package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

public class SearchProjectResultsDeveloper extends SearchProjectResultsUser{

    public SearchProjectResultsDeveloper(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectsIds, List<String> projectsNames) {

        super(mainFrame, homePanel, projectsIds, projectsNames);
    }

    @Override
    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/frontend/gui/images/reportIssue.png",
                "Reported", "/frontend/gui/images/reportedIssues.png",
                "Assigned", "/frontend/gui/images/assignedIssues.png"
        );
    }

    @Override
    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported", "Assigned"};
    }

    @Override
    protected ProjectTableModelUser createTableModel(List<Integer> projectIds, List<String> projectNames) {

        int numRows = projectIds.size();
        Object[][] rowData = new Object[numRows][5];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = projectIds.get(i);
            rowData[i][1] = projectNames.get(i);
            rowData[i][2] = "Report";
            rowData[i][3] = "Reported";
            rowData[i][4] = "Assigned";
        }

        return new ProjectTableModelDeveloper(rowData);
    }

    @Override
    protected JTable createTable(JFrame mainFrame, HomePanelUser homePanel, List<Integer> projectIds, List<String> projectNames) {

        JTable resultsTable = new JTable(createTableModel(projectIds, projectNames));

        for (int i = 0; i < buttonActions.length; i++) {

            int columnIndex = 2 + i;
            String actionName = buttonActions[i];
            String iconUrl = ICON_URL_MAP.get(actionName);

            TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(columnIndex);

            buttonColumn.setCellRenderer(new IconCellRenderer(iconUrl, ICON_WIDTH, ICON_HEIGHT));
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
