package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {

    protected RoundedPanel mainPanel;

    public MyDialog(JFrame parent) {

        super(parent, true);

        setDialog();
        setMainPanel(parent);

        setBackButton();
    }

    private void setDialog() {

        setUndecorated(true);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setMainPanel(JFrame parent) {

        mainPanel = new RoundedPanel(new GridBagLayout());

        mainPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        mainPanel.setBackground(ColorsList.BACKGROUND_COLOR);

        mainPanel.setMinimumSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));
        mainPanel.setPreferredSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));

        setContentPane(mainPanel);
    }

    protected void setBackButton() {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> backActionListener());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.FIRST_LINE_START, new Insets(5, 5, 5, 5));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());
    }

    protected void backActionListener() {

        dispose();
    }
}
