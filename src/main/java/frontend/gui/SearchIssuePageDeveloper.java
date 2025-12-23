package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchIssuePageDeveloper extends SearchIssuePageUser {

    public SearchIssuePageDeveloper(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        IssueSearchPanelUser issueSearchPanel = new IssueSearchPanelDeveloper(mainFrame);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }
}
