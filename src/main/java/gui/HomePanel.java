package gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HomePanel {

    protected static HomePanel instance;
    protected RoundedPanel homePanel;
    protected SearchProjectViewResults searchProjectViewResults;
    protected final Color BorderColor = new Color (77, 133, 255);
    protected final Color BgColor = new Color(230, 238, 255);

    protected HomePanel() {

        setHomePanel();
        setLogOutButton();
        setSearchProjectPanel();
        setSearchProjectViewResults();
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

    private void setSearchProjectPanel() {

        SearchProjectPanel searchProjectPanel = new SearchProjectPanel();

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        homePanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }

    private void setSearchProjectViewResults() {

        searchProjectViewResults = new SearchProjectViewResults();

        Constraints.setConstraints(0, 2, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.1f, 0.9f, new Insets(10, 30, 10, 30));
        homePanel.add(searchProjectViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    public void updateSearchProjectViewResults(Component component) {

        //todo: funzione che permette di aggiornare la vista dei risultati della ricerca
        searchProjectViewResults.updateViewportView(component);
    }

    public JPanel getHomePanel() {
        return homePanel;
    }

    public static HomePanel getInstance() {

        if (instance == null)
            instance = new HomePanel();

        return instance;
    }
}
