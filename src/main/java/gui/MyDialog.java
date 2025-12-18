package gui;

import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {

    protected RoundedPanel contentPanel;

    public MyDialog(JFrame owner) {

        super(owner, true);
        setUndecorated(true);

        setBackground(ColorsList.EMPTY_COLOR);

        contentPanel = new RoundedPanel(new GridBagLayout());

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        setContentPane(contentPanel);

        pack();
        setLocationRelativeTo(getOwner());
    }
}
