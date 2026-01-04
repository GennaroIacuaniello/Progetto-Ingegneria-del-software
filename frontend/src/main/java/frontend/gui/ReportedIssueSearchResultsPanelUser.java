package frontend.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class ReportedIssueSearchResultsPanelUser {

    protected static final int ICON_WIDTH = 20;
    protected static final int ICON_HEIGHT = 20;

    public ReportedIssueSearchResultsPanelUser(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        searchPage.updateSearchIssueViewResults(createTable(mainFrame, issuesTitles));
    }

    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));
        buttonColumn.setCellEditor(new IconCellEditorReportedIssueUser(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        JTableHeader header = resultsTable.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        return resultsTable;
    }

    protected IssueTableModel createTableModel(List<String> issueTitles) {

        int numRows = issueTitles.size();
        Object[][] rowData = new Object[numRows][2];

        for (int i = 0; i < numRows; i++) {
            rowData[i][0] = issueTitles.get(i);
            rowData[i][1] = "View";
        }

        return new IssueTableModel(rowData);
    }
}
