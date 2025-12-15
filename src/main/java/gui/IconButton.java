package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class IconButton extends JButton {

    public IconButton(String url, int width, int height) {

        super();

        removeListeners();
        setImageIcon(url, width, height);
        setBackground();
    }

    private void removeListeners() {

        for (FocusListener focusListener : getFocusListeners())
            this.removeFocusListener(focusListener);

        for (MouseListener mouseListener : getMouseListeners())
            this.removeMouseListener(mouseListener);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    fireActionPerformed(new ActionEvent(
                            IconButton.this,
                            ActionEvent.ACTION_PERFORMED,
                            getText()));
                }
            }
        });
    }

    private void setImageIcon(String url, int width, int height) {

        URL imageURL = getClass().getResource(url);
        Image image = new ImageIcon(imageURL).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(image);

        this.setIcon(imageIcon);
    }

    private void setBackground() {

        //this.setBackground(new Color(0, 0, 0, 0));
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
}
