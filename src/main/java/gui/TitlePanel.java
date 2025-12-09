package gui;

import javax.swing.*;
import java.awt.*;

public class TitlePanel {

    private final JPanel titlePanel = new JPanel();
    private final JLabel titleLabel = new JLabel("titolo");

    public TitlePanel() {

        titlePanel.setLayout(new GridBagLayout());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER);
        titlePanel.add(titleLabel, Constraints.getGridBagConstraints());

        titlePanel.setVisible(true);
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }
}
