package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchReportedIssuePageUser extends RoundedPanel{

    public SearchReportedIssuePageUser(JFrame mainFrame, HomePanelUser homePanel) {

        super(new GridBagLayout());

        setPanel();
        setIssueSearchPanel(mainFrame);
        setIssueSearchResultsPanel();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        this.setBackground(ColorsList.EMPTY_COLOR);
    }

    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new ReportedIssueSearchPanelUser(mainFrame);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }

    protected void setIssueSearchResultsPanel() {

        ReportedIssueSearchResultsPanelUser reportedIssueSearchResultsPanelUser = new ReportedIssueSearchResultsPanelUser();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.VERTICAL, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 0, 10, 0));
        this.add(reportedIssueSearchResultsPanelUser, Constraints.getGridBagConstraints());
    }
}
