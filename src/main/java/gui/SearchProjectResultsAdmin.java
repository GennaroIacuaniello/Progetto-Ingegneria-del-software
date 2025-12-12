package gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;

public class SearchProjectResultsAdmin extends SearchProjectResultsDeveloper{

    public SearchProjectResultsAdmin(List<String > projectsIds, List<String> projectsNames) {

        super(projectsIds, projectsNames);
    }

    @Override
    protected void setIconUrlMap() {

        ICON_URL_MAP = Map.of(
                "Report", "/gui/images/reportIssue.png",
                "Reported", "/gui/images/reportedIssues.png",
                "Assigned", "/gui/images/assignedIssues.png",
                "View", "/gui/images/viewIssues.png"
        );
    }

    @Override
    protected void setButtonActions() {

        buttonActions = new String[]{"Report", "Reported", "Assigned", "View"};
    }

    @Override
    protected ProjectTableModelUser createTableModel(List<String> projectIds, List<String> projectNames) {

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

        return new ProjectTableModelAdmin(rowData);
    }
}
