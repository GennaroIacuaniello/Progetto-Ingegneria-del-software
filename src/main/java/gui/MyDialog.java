package gui;

import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {

    protected final Color BorderColor = new Color (77, 133, 255);
    protected RoundedPanel contentPanel;

    public MyDialog() {

        /*super(HomePage.getInstance().getMainFrame(), true);
        setUndecorated(true);

        setBackground(new Color(0, 0, 0, 0));

        contentPanel = new RoundedPanel(new GridBagLayout());

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setRoundBorderColor(BorderColor);

        setContentPane(contentPanel);

        pack();
        setLocationRelativeTo(getOwner());*/
    }
}
