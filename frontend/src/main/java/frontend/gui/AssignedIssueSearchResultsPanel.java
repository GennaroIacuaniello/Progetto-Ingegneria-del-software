package frontend.gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;

public class AssignedIssueSearchResultsPanel extends ReportedIssueSearchResultsPanelDeveloper {

    public AssignedIssueSearchResultsPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        super(mainFrame, searchPage, issuesTitles);
    }

    @Override
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/reportIssue.png", ICON_WIDTH, ICON_HEIGHT));
        buttonColumn.setCellEditor(new IconCellEditorAssignedIssue(mainFrame, "/frontend/gui/images/reportIssue.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        resultsTable.getTableHeader().setReorderingAllowed(false);

        return resultsTable;
    }
}
