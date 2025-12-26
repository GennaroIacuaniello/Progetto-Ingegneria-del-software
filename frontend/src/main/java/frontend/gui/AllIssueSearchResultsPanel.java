package frontend.gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;

public class AllIssueSearchResultsPanel extends AssignedIssueSearchResultsPanel {

    public AllIssueSearchResultsPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage, List<String> issuesTitles) {

        super(mainFrame, searchPage, issuesTitles);
    }

    @Override
    protected JTable createTable(JFrame mainFrame, List<String> issuesTitles) {

        JTable resultsTable = new JTable(createTableModel(issuesTitles));

        TableColumn buttonColumn = resultsTable.getColumnModel().getColumn(1);

        buttonColumn.setCellRenderer(new IconCellRenderer("/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT));
        buttonColumn.setCellEditor(new IconCellEditorAllIssue(mainFrame, "/frontend/gui/images/showIssueIconButton.png", ICON_WIDTH, ICON_HEIGHT, resultsTable));

        resultsTable.setRowHeight(ICON_HEIGHT + 4);

        resultsTable.getTableHeader().setReorderingAllowed(false);

        return resultsTable;
    }
}
