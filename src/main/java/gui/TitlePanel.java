package gui;

import javax.swing.*;
import java.awt.*;

public class TitlePanel {

    private final JPanel titlePanel = new JPanel();
    private final JLabel titleLabel = new JLabel("titolo");
    private final Color bgColor = new Color(204, 239, 255);

    public TitlePanel() {

        titlePanel.setLayout(new GridBagLayout());

        titlePanel.setBackground(bgColor);
        //titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addTitleLabel();

        titlePanel.setVisible(true);
    }

    private void addTitleLabel() {

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER);
        titlePanel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }
}
