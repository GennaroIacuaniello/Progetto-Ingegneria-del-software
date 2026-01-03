package frontend.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class IconButton extends JButton {

    private Image originalImage;
    private FlatSVGIcon svgIcon;
    private final int targetWidth;
    private final int targetHeight;

    public IconButton(String url, int width, int height) {

        super();

        this.targetWidth = width;
        this.targetHeight = height;

        removeListeners();
        setImageIcon(url);
        setupStyle();
    }

    private void setImageIcon(String url) {

        if (url.toLowerCase().endsWith(".svg")) {

            URL res = getClass().getResource(url);

            if (res != null)
                svgIcon = new FlatSVGIcon(res);
        } else {

            URL imageURL = getClass().getResource(url);

            if (imageURL != null)
                this.originalImage = new ImageIcon(imageURL).getImage();
        }

        int padding = 2;

        Dimension d = new Dimension(targetWidth + padding, targetHeight + padding);

        setPreferredSize(d);
        setMinimumSize(d);

        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int w = getWidth();
        int h = getHeight();

        int margin = 2; //se l'immagine è troppo grande o tagliata modifica questo
        int finalW = Math.min(targetWidth, w - margin);
        int finalH = Math.min(targetHeight, h - margin);

        int x = (w - finalW) / 2;
        int y = (h - finalH) / 2;

        if (svgIcon != null) {

            Image svgImage = svgIcon.derive(finalW, finalH).getImage();

            if (svgImage != null)
                g2.drawImage(svgImage, x, y, finalW, finalH, null);

        } else if (originalImage != null)
            g2.drawImage(originalImage, x, y, finalW, finalH, this);

        g2.dispose();
    }

    private void setupStyle() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
        setBorder(null);
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
}