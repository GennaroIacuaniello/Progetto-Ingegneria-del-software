package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanel {

    private static HomePanel instance;
    private RoundedPanel homePanel;
    private SearchProjectPanel searchProjectPanel;

    private HomePanel() {

        setHomePanel();
        setLogOutButton();
        setSearchProjectPanel();
    }

    private void setHomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(Color.WHITE);
        homePanel.setRoundBorderColor(new Color(153, 255, 255));
    }

    private void setLogOutButton() {
        //todo
    }

    private void setSearchProjectPanel() {

        //todo: completa
        searchProjectPanel = new SearchProjectPanel();
    }

    public JPanel getHomePanel() {
        return homePanel;
    }

    public static HomePanel getInstance() {

        if (instance == null)
            instance = new HomePanel();

        return instance;
    }

    public SearchProjectPanel getSearchProjectPanel() {
        return searchProjectPanel;
    }
}
