package gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LogOutButton extends JButton {

    public LogOutButton() {

        super();

        setImageIcon();
        setBackground();
        setActionListener();
    }

    private void setImageIcon() {

        URL imageURL = getClass().getResource("/gui/images/logOutButtonIcon.png");
        Image image = new ImageIcon(imageURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(image);

        this.setIcon(imageIcon);
    }

    private void setBackground() {

        this.setBackground(new Color(0, 0, 0, 0));
        this.setForeground(new Color(0, 0, 0, 0));
        this.setBorderPainted(false);
    }

    private void setActionListener() {
        //todo: implementa
    }
}
