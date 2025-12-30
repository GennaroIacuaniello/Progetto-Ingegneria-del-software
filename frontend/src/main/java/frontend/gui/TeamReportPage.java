package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TeamReportPage extends RoundedPanel{

    HomePanelUser homePanel;
    SearchViewResults searchViewResults;

    public TeamReportPage(HomePanelUser homePanel) {

        super(new GridBagLayout());

        this.homePanel = homePanel;

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

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.5f, 0.5f,
                new Insets(10, 80, 10, 80));
        this.add(teamReportSearchPanel, Constraints.getGridBagConstraints());
    }

    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("<html><center>Effettua una ricerca<br>" +
                                                              "per visualizzare i risultati</html>");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.VERTICAL, 0, 0, GridBagConstraints.CENTER,
                1f, 1f);
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void updateReportViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    public void homePanelReturnToDefaultContentPane() {

        homePanel.returnToDefaultContentPanel();
    }
}
