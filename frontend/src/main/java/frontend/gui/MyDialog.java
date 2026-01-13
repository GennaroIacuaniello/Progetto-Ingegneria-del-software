package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Classe base personalizzata per le finestre di dialogo modali.
 * <p>
 * Questa classe estende {@link JDialog} e funge da modello (template) per tutte le finestre secondarie
 * dell'applicazione (es. Dashboard, Report).
 * Le sue caratteristiche principali sono:
 * </p>
 * <ul>
 * <li><b>Undecorated:</b> Rimuove i bordi e la barra del titolo del sistema operativo.</li>
 * <li><b>Aspetto coerente:</b> Utilizza un {@link RoundedPanel} come contenitore principale per dare l'effetto "angoli arrotondati".</li>
 * <li><b>Dimensionamento relativo:</b> Si ridimensiona automaticamente al 75% delle dimensioni della finestra genitore.</li>
 * <li><b>Navigazione:</b> Include di default un pulsante "Indietro" per chiudere la finestra.</li>
 * </ul>
 */
public class MyDialog extends JDialog {

    /**
     * Il pannello principale con bordi arrotondati che funge da ContentPane.
     * È {@code protected} per permettere alle sottoclassi di aggiungere i propri componenti.
     */
    protected RoundedPanel mainPanel;

    /**
     * Costruttore della classe base.
     * <p>
     * Inizializza il dialogo come modale (blocca l'interazione con la finestra padre),
     * rimuove le decorazioni, imposta il pannello principale e aggiunge il pulsante di navigazione.
     * </p>
     *
     * @param parent Il frame principale (owner) su cui si apre questo dialogo.
     */
    public MyDialog(JFrame parent) {

        super(parent, true); // true = modale

        setDialog();
        setMainPanel(parent);

        setBackButton();
    }

    /**
     * Rimuove le decorazioni standard della finestra.
     * <p>
     * Imposta {@code setUndecorated(true)} per nascondere barra del titolo e bordi di sistema.
     * Imposta lo sfondo del JDialog a trasparente (EMPTY_COLOR) per permettere al
     * {@link RoundedPanel} sovrastante di disegnare correttamente i bordi arrotondati senza artefatti rettangolari.
     * </p>
     */
    private void setDialog() {

        setUndecorated(true);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    /**
     * Configura il pannello contenitore principale.
     * <p>
     * Crea un {@link RoundedPanel}, imposta i colori di sfondo e bordo,
     * calcola le dimensioni (75% larghezza e altezza del parent) e lo imposta come ContentPane.
     * </p>
     *
     * @param parent Il frame genitore usato come riferimento per il ridimensionamento.
     */
    private void setMainPanel(JFrame parent) {

        mainPanel = new RoundedPanel(new GridBagLayout());

        mainPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        mainPanel.setBackground(ColorsList.BACKGROUND_COLOR);

        // Calcola dimensioni relative (75% del parent)
        mainPanel.setMinimumSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));
        mainPanel.setPreferredSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));

        setContentPane(mainPanel);
    }

    /**
     * Aggiunge il pulsante standard "Indietro" nell'angolo in alto a sinistra.
     * <p>
     * È marcato {@code protected} nel caso le sottoclassi vogliano sovrascriverlo o rimuoverlo,
     * anche se il comportamento standard è fornito qui.
     * </p>
     */
    protected void setBackButton() {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> backActionListener());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.FIRST_LINE_START, new Insets(5, 5, 5, 5));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());
    }

    /**
     * Definisce l'azione eseguita dal pulsante indietro.
     * <p>
     * Di base chiude la finestra correntemente aperta ({@code dispose()}).
     * Le sottoclassi possono sovrascrivere questo metodo per logiche di chiusura personalizzate
     * (es chiedere conferma salvataggio).
     * </p>
     */
    protected void backActionListener() {

        dispose();
    }
}