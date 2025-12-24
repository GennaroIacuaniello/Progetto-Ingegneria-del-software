package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TitlePanel {

    private final RoundedPanel titlePanel = new RoundedPanel(new GridBagLayout());
    private final JLabel titleLabel = new JLabel("titolo");
    private final Color bgColor = new Color(153, 201, 255);
    private final Color BorderColor = new Color (77, 133, 255);

    public TitlePanel() {

        titlePanel.setBackground(bgColor);
        titlePanel.setRoundBorderColor(BorderColor);

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
