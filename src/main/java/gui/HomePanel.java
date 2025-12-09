package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanel {

    private static HomePanel instance;
    private RoundedPanel homePanel;

    private HomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(Color.WHITE);
        homePanel.setRoundBorderColor(new Color(153, 255, 255));

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
