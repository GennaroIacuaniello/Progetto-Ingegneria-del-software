package gui;

import com.formdev.flatlaf.util.HSLColor;

import javax.swing.*;
import java.awt.*;

public class HomePanel {

    private static HomePanel instance;
    private RoundedPanel homePanel;
    private SearchProjectPanel searchProjectPanel;
    private JButton logOutButton;
    private final Color BorderColor = new Color (77, 133, 255);
    private final Color BgColor = new Color(230, 238, 255);

    private HomePanel() {

        setHomePanel();
        setLogOutButton();
        setSearchProjectPanel();
    }

    private void setHomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(BgColor);
        homePanel.setRoundBorderColor(BorderColor);
    }

    private void setLogOutButton() {
        //todo aggiungi action listener
        logOutButton = new JButton("Log out");

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(10, 0, 0, 10));
        homePanel.add(logOutButton, Constraints.getGridBagConstraints());
    }

    private void setSearchProjectPanel() {

        searchProjectPanel = new SearchProjectPanel();

        //todo: la barra non si deve buggare su resize
        searchProjectPanel.getSearchTextField().setSize(300, 50);

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.5f);
        homePanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
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
