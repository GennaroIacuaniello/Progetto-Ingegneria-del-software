package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchAssignedIssuePage extends SearchReportedIssuePageDeveloper{

    public SearchAssignedIssuePage(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new AssignedIssueSearchPanel(mainFrame);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }

    @Override
    protected void setIssueSearchResultsPanel() {

        ReportedIssueSearchResultsPanelUser reportedIssueSearchResultsPanelDeveloper = new AssignedIssueSearchResultsPanel();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.VERTICAL, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 0, 10, 0));
        this.add(reportedIssueSearchResultsPanelDeveloper, Constraints.getGridBagConstraints());
    }
}
