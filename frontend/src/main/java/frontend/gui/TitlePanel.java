package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class TitlePanel {

    private static TitlePanel instance;
    private static final RoundedPanel titlePanel = new RoundedPanel(new GridBagLayout());
    private static final JLabel titleLabel = new JLabel("titolo");

    private TitlePanel() {

        titlePanel.setBackground(ColorsList.TITLE_BACKGROUND_COLOR);
        titlePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        addTitleLabel();

        titlePanel.setVisible(true);
    }

    private void addTitleLabel() {

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER);
        titlePanel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }

    public void setTitle(String title) {

        titleLabel.setText(title);
    }

    public static TitlePanel getInstance() {

        if (instance == null)
            instance = new TitlePanel();

        return instance;
    }
}
