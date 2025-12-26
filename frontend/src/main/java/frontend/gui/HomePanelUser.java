package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class HomePanelUser {

    protected RoundedPanel defaultContentPanel;
    protected RoundedPanel contentPanel;
    protected RoundedPanel homePanel;
    protected SearchViewResults searchViewResults;

    protected HomePanelUser(JFrame mainFrame) {

        setHomePanel();
        setLogOutButton();
        setDefaultContentPanel(mainFrame);
    }

    private void setHomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(ColorsList.BACKGROUND_COLOR);
        homePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
    }

    private void setLogOutButton() {
        //todo aggiungi action listener

        LogOutButton logOutButton = new LogOutButton();

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(10, 0, 0, 10));
        homePanel.add(logOutButton, Constraints.getGridBagConstraints());
    }

    private void setDefaultContentPanel(JFrame mainFrame) {

        contentPanel = new RoundedPanel(new GridBagLayout());
        contentPanel.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        contentPanel.setBackground(ColorsList.EMPTY_COLOR);

        setSearchProjectPanel(mainFrame);
        setSearchProjectViewResults();

        setContentPanel(contentPanel);
        defaultContentPanel = contentPanel;
    }

    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelUser(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }

    private void setSearchProjectViewResults() {

        searchViewResults = new SearchViewResults();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 0.1f, 1f);
        contentPanel.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void updateSearchProjectViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    public RoundedPanel getHomePanel() {
        return homePanel;
    }

    public void setContentPanel(RoundedPanel panel) {

        homePanel.remove(contentPanel);

        contentPanel = panel;

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 1f, 1f,
                new Insets(10, 10, 10, 10));
        homePanel.add(contentPanel, Constraints.getGridBagConstraints());

        homePanel.revalidate();
        homePanel.repaint();
    }

    public void returnToDefaultContentPanel() {

        setContentPanel(defaultContentPanel);
    }
}
