package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchReportedIssuePageUser extends RoundedPanel{

    private SearchViewResults searchViewResults;
    private final HomePanelUser homePanelUser;

    public SearchReportedIssuePageUser(JFrame mainFrame, HomePanelUser homePanel) {

        super(new GridBagLayout());

        this.homePanelUser = homePanel;

        setPanel();
        setIssueSearchPanel(mainFrame);
        setIssueSearchResultsPanel();
        setTitlePanel();
    }

    protected void setTitlePanel() {

        TitlePanel.getInstance().setTitle("ISSUE REPORTATE");
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        this.setBackground(ColorsList.EMPTY_COLOR);
    }

    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new ReportedIssueSearchPanelUser(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }

    protected void setIssueSearchResultsPanel() {

        searchViewResults = new SearchViewResults();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new  Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void updateSearchIssueViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    public void homePanelReturnToDefaultContentPane() {

        homePanelUser.returnToDefaultContentPanel();
    }
}
