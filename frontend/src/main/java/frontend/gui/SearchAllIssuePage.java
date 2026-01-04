package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchAllIssuePage extends SearchAssignedIssuePage{

    public SearchAllIssuePage(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setTitlePanel() {

        TitlePanel.getInstance().setTitle("VEDI TUTTE LE ISSUE");
    }

    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new AllIssueSearchPanel(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }
}
