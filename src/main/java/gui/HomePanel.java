package gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HomePanel {

    protected JScrollPane contentScrollPane;
    protected RoundedPanel projectsPanel;
    protected RoundedPanel homePanel;
    protected SearchProjectViewResults searchProjectViewResults;
    protected final Color BorderColor = new Color (77, 133, 255);
    protected final Color BgColor = new Color(230, 238, 255);

    protected HomePanel() {

        setHomePanel();
        setLogOutButton();
        setProjectsPanel();
        setScrollPane();
    }

    private void setHomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(BgColor);
        homePanel.setRoundBorderColor(BorderColor);
    }

    private void setLogOutButton() {
        //todo aggiungi action listener

        LogOutButton logOutButton = new LogOutButton();

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(10, 0, 0, 10));
        homePanel.add(logOutButton, Constraints.getGridBagConstraints());
    }

    private void setProjectsPanel() {

        projectsPanel = new RoundedPanel(new GridBagLayout());
        projectsPanel.setRoundBorderColor(new  Color(0, 0, 0,0));
        projectsPanel.setBackground(new Color(0, 0, 0,0));

        setSearchProjectPanel();
        setSearchProjectViewResults();
    }

    private void setSearchProjectPanel() {

        SearchProjectPanel searchProjectPanel = new SearchProjectPanel(this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        projectsPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }

    private void setSearchProjectViewResults() {

        searchProjectViewResults = new SearchProjectViewResults();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 0.1f, 1f);
        projectsPanel.add(searchProjectViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    private void setScrollPane() {

        contentScrollPane = new JScrollPane();
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.getViewport().setBackground(new Color(0, 0, 0,0));

        contentScrollPane.setViewportView(projectsPanel);

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.1f, 1f, new Insets(0, 10, 10, 10));
        homePanel.add(contentScrollPane, Constraints.getGridBagConstraints());
    }

    public void updateSearchProjectViewResults(Component component) {

        //todo: funzione che permette di aggiornare la vista dei risultati della ricerca
        searchProjectViewResults.updateViewportView(component);
    }

    public JPanel getHomePanel() {
        return homePanel;
    }

    public JScrollPane getScrollPane() {
        return contentScrollPane;
    }
}
