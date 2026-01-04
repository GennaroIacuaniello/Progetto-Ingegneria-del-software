package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TeamReportPage extends RoundedPanel{

    ManageTeamsPanel manageTeamsPanel;
    SearchViewResults searchViewResults;

    public TeamReportPage(ManageTeamsPanel manageTeamsPanel) {

        super(new GridBagLayout());

        this.manageTeamsPanel = manageTeamsPanel;

        setPanel();
        setTeamReportSearchPanel();
        setSearchViewResults();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        this.setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setTeamReportSearchPanel() {

        TeamReportSearchPanel teamReportSearchPanel = new TeamReportSearchPanel(this);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                32, 32, GridBagConstraints.CENTER, 0.5f, 0.5f,
                new Insets(10, 80, 10, 80));
        this.add(teamReportSearchPanel, Constraints.getGridBagConstraints());
    }

    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("<html><center>Effettua una ricerca<br>" +
                                                              "per visualizzare i risultati</html>");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new  Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void updateReportViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    public void returnToManageTeamsPanel() {

        manageTeamsPanel.returnToManageTeamsPanel();
    }
}
