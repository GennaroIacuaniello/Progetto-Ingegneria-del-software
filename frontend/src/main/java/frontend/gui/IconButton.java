package frontend.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Componente personalizzato per la creazione di pulsanti icona.
 * <p>
 * Questa classe estende {@link JButton} per creare pulsanti costituiti esclusivamente da un'immagine,
 * privi dei classici bordi, sfondi e feedback visivi dei bottoni standard di Swing.
 * Supporta nativamente:
 * <ul>
 * <li><b>File SVG:</b> Utilizzando {@link FlatSVGIcon} per un rendering nitido a qualsiasi dimensione.</li>
 * <li><b>Immagini Raster (PNG, JPG):</b> Utilizzando {@link ImageIcon} standard.</li>
 * </ul>
 * La classe gestisce autonomamente il ridimensionamento, il centraggio e l'antialiasing dell'icona.
 * </p>
 */
public class IconButton extends JButton {

    /**
     * Immagine raster (se il file non è un SVG).
     */
    private Image originalImage;

    /**
     * Icona vettoriale (se il file è un SVG), fornita da FlatLaf.
     */
    private FlatSVGIcon svgIcon;

    /**
     * Larghezza desiderata per l'icona.
     */
    private final int targetWidth;

    /**
     * Altezza desiderata per l'icona.
     */
    private final int targetHeight;

    /**
     * Costruttore del pulsante icona.
     * <p>
     * Inizializza il pulsante, rimuove i listener di default per un controllo personalizzato,
     * carica l'immagine dal percorso specificato e applica lo stile "invisibile" al contenitore.
     * </p>
     *
     * @param url    Il percorso della risorsa immagine (relativo al classpath).
     * @param width  La larghezza desiderata dell'icona.
     * @param height L'altezza desiderata dell'icona.
     */
    public IconButton(String url, int width, int height) {

        super();

        this.targetWidth = width;
        this.targetHeight = height;

        removeListeners();
        setImageIcon(url);
        setupStyle();
    }

    /**
     * Carica l'immagine o l'icona in base all'estensione del file.
     * <p>
     * Se l'URL termina con ".svg", viene creato un {@link FlatSVGIcon}.
     * Altrimenti, viene caricata una normale {@link Image}.
     * Imposta inoltre le dimensioni preferite del componente aggiungendo un piccolo padding.
     * </p>
     *
     * @param url Il percorso della risorsa.
     */
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

    /**
     * Gestisce il rendering personalizzato del componente.
     * <p>
     * Sovrascrive il metodo di disegno standard per:
     * <ol>
     * <li>Attivare l'antialiasing e l'interpolazione bicubica per la massima qualità visiva.</li>
     * <li>Calcolare la posizione per centrare perfettamente l'immagine nel pulsante.</li>
     * <li>Disegnare l'SVG (ridimensionato dinamicamente) o l'immagine raster.</li>
     * </ol>
     * </p>
     *
     * @param g Il contesto grafico utilizzato per disegnare.
     */
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Miglioramento della qualità grafica
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int w = getWidth();
        int h = getHeight();

        int margin = 2; // Margine di sicurezza per evitare tagli ai bordi
        int finalW = Math.min(targetWidth, w - margin);
        int finalH = Math.min(targetHeight, h - margin);

        // Calcolo coordinate per il centraggio
        int x = (w - finalW) / 2;
        int y = (h - finalH) / 2;

        if (svgIcon != null) {
            // Se è un SVG, deriva una nuova istanza scalata alle dimensioni finali
            Image svgImage = svgIcon.derive(finalW, finalH).getImage();

            if (svgImage != null)
                g2.drawImage(svgImage, x, y, finalW, finalH, null);

        } else if (originalImage != null)
            // Se è raster, disegna l'immagine scalata
            g2.drawImage(originalImage, x, y, finalW, finalH, this);

        g2.dispose();
    }

    /**
     * Applica lo stile visivo per rendere il pulsante simile a una semplice immagine cliccabile.
     * <p>
     * Rimuove il riempimento dello sfondo, i bordi dipinti, il focus visivo e imposta il cursore a mano.
     * </p>
     */
    private void setupStyle() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
        setBorder(null);
    }

    /**
     * Rimuove i listener predefiniti e ne aggiunge uno personalizzato.
     * <p>
     * Questa operazione serve a rimuovere i comportamenti standard di Swing che potrebbero
     * interferire con l'aspetto personalizzato. Viene aggiunto un nuovo {@link MouseAdapter}
     * che scaturisce l'evento {@code ACTION_PERFORMED} solo al rilascio del mouse (MouseReleased),
     * garantendo un'interazione pulita.
     * </p>
     */
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