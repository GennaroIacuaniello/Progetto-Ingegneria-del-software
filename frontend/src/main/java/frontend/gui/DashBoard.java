package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import java.awt.*;


public class DashBoard extends MyDialog {

    SearchViewResults searchViewResults;

    protected RoundedPanel mainPanel;

    public DashBoard(JFrame parent) {

        super(parent);

        ProjectController.getInstance().createDashBoard();

        setSearchViewResults();

        new ReportResults(this);
    }

    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.VERTICAL, 0, 0, GridBagConstraints.CENTER,
                1f, 1f);
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void setDashBoardViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }
}
