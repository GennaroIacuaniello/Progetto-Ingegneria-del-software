package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class IconButton extends JButton {

    private Image originalImage;
    private final int targetWidth;
    private final int targetHeight;

    public IconButton(String url, int width, int height) {

        super();

        targetWidth = width;
        targetHeight = height;

        removeListeners();
        setImageIcon(url);
        setupStyle();
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

    private void setImageIcon(String url) {

        URL imageURL = getClass().getResource(url);

        if (imageURL != null) {

            ImageIcon tempIcon = new ImageIcon(imageURL);
            originalImage = tempIcon.getImage();

            Dimension d = new Dimension(targetWidth, targetHeight);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
        }
    }

    private void setupStyle() {

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (originalImage != null) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int x = (getWidth() - targetWidth) / 2;
            int y = (getHeight() - targetHeight) / 2;

            g2.drawImage(originalImage, x, y, targetWidth, targetHeight, this);

            g2.dispose();
        }
    }
}
