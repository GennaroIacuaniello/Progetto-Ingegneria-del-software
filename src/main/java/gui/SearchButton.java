package gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SearchButton extends JButton {

    public SearchButton() {

        super();

        setImageIcon();
        setBackground();
        setActionListener();
    }

    private void setImageIcon() {

        URL imageURL = getClass().getResource("/gui/images/searchButton.png");
        Image image = new ImageIcon(imageURL).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
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
