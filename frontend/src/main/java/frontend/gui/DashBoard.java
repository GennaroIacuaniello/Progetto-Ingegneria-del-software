package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DashBoard extends MyDialog {

    SearchViewResults searchViewResults;

    protected RoundedPanel mainPanel;

    public DashBoard(JFrame parent) {

        super(parent);

        ControllerTMP.createDashBoard();

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
