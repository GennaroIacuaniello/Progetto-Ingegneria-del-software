package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import java.awt.*;


public class DashBoard extends MyDialog {

    SearchViewResults searchViewResults;

    protected RoundedPanel mainPanel;

    public DashBoard(JFrame parent) {

        super(parent);

        boolean success = ProjectController.getInstance().createDashBoard();

        if(!success)
            return;

        setSearchViewResults();

        new ReportResults(this);
    }

    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void setDashBoardViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }
}
