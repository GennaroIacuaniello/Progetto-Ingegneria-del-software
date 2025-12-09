package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanel {

    private static HomePanel instance;
    private JPanel homePanel;

    private HomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        //homePanel.setLayout(new GridBagLayout());
        homePanel.setBackground(Color.WHITE);

        //todo rimuovi testLabel
        JLabel testLabel = new JLabel("contenuto");
        testLabel.setHorizontalAlignment(SwingConstants.CENTER);
        testLabel.setVerticalAlignment(SwingConstants.CENTER);

        homePanel.add(testLabel);
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
