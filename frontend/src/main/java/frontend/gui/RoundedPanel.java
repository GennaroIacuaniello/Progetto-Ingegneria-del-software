package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello personalizzato con angoli arrotondati.
 * <p>
 * Questa classe estende {@link JPanel} per fornire un contenitore grafico con bordi smussati (rounded corners).
 * A differenza di un pannello standard, che è sempre rettangolare e opaco, questo componente:
 * <ul>
 * <li>Disabilita l'opacità standard per permettere la trasparenza negli angoli (fuori dalla curva).</li>
 * <li>Sovrascrive il metodo {@link #paintComponent(Graphics)} per disegnare manualmente lo sfondo e il bordo arrotondato.</li>
 * <li>Utilizza l'anti-aliasing per garantire che le curve siano disegnate in modo fluido e non "pixellato".</li>
 * </ul>
 * </p>
 */
public class RoundedPanel extends JPanel {

    /**
     * Il colore di riempimento dello sfondo del pannello.
     */
    private Color backgroundColor;

    /**
     * Il colore del bordo arrotondato.
     */
    private Color roundBorderColor;

    /**
     * Costruttore del pannello arrotondato.
     * <p>
     * Inizializza il pannello con un LayoutManager specifico.
     * <b>Nota Importante:</b> Chiama {@code setOpaque(false)}. Questo è fondamentale perché, se il pannello fosse opaco,
     * Swing disegnerebbe un rettangolo di sfondo predefinito che coprirebbe gli angoli arrotondati, rovinando l'effetto.
     * </p>
     *
     * @param layout Il gestore del layout da applicare al pannello (es. GridBagLayout).
     */
    public RoundedPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        this.backgroundColor = getBackground();
        this.roundBorderColor = getForeground();
    }

    /**
     * Metodo di disegno del componente.
     * <p>
     * Questo è il cuore della personalizzazione grafica. Viene invocato automaticamente da Swing ogni volta
     * che il componente deve essere ridisegnato.
     * <ol>
     * <li>Attiva l'<b>Anti-aliasing</b> per rendere le linee curve morbide.</li>
     * <li>Disegna il rettangolo arrotondato di sfondo (fillRoundRect).</li>
     * <li>Disegna il bordo arrotondato (drawRoundRect) sopra lo sfondo.</li>
     * </ol>
     * Il raggio degli angoli è fissato hardcoded a 30 pixel.
     * </p>
     *
     * @param g Il contesto grafico utilizzato per disegnare.
     */
    @Override
    protected void paintComponent(Graphics g) {
        int cornerRadius = 30; // Raggio dell'arrotondamento

        int width = getWidth();
        int height = getHeight();

        Graphics2D graphics = (Graphics2D) g;
        // Attiva l'antialiasing per bordi lisci
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Disegna lo sfondo
        graphics.setColor(backgroundColor);
        // width-1 e height-1 servono per rimanere entro i limiti del componente
        graphics.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        // 2. Disegna il bordo
        graphics.setColor(roundBorderColor);
        //graphics.setStroke(new BasicStroke(1.5f)); // Opzionale: spessore bordo
        graphics.drawRoundRect(1, 1, width - 3, height - 3, cornerRadius, cornerRadius);
    }

    /**
     * Imposta il colore del bordo arrotondato e forza il ridisegno.
     *
     * @param color Il nuovo colore del bordo.
     */
    public void setRoundBorderColor(Color color) {
        this.roundBorderColor = color;
        repaint();
    }

    /**
     * Imposta il colore di sfondo.
     * <p>
     * Sovrascrive il metodo standard per aggiornare anche la variabile interna {@code backgroundColor}
     * utilizzata nel metodo di painting personalizzato.
     * </p>
     *
     * @param bg Il nuovo colore di sfondo.
     */
    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        super.setBackground(bg);
        repaint();
    }

    /**
     * Restituisce il colore di sfondo corrente.
     */
    @Override
    public Color getBackground() {
        return backgroundColor;
    }
}