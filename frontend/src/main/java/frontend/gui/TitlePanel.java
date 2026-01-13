package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Gestore del pannello del Titolo (Intestazione) dell'applicazione.
 * <p>
 * Questa classe implementa il pattern <b>Singleton</b> per gestire la barra superiore dell'interfaccia.
 * Il pannello funge da intestazione globale: viene istanziato una sola volta e rimane persistente,
 * permettendo alle diverse schermate di aggiornare semplicemente il testo (es. "HOME", "REPORT MENSILE")
 * tramite il metodo {@link #setTitle(String)}.
 * </p>
 */
@SuppressWarnings("java:S6548")
public class TitlePanel {

    /**
     * L'unica istanza della classe (Singleton).
     */
    private static TitlePanel instance;

    /**
     * Il contenitore grafico arrotondato.
     */
    private static final RoundedPanel titlePanel = new RoundedPanel(new GridBagLayout());

    /**
     * L'etichetta che mostra il testo effettivo.
     */
    private static final JLabel titleLabel = new JLabel("titolo");

    /**
     * Costruttore privato (Singleton).
     * <p>
     * Inizializza lo stile del pannello (sfondo, bordi) e aggiunge l'etichetta di testo.
     * </p>
     */
    private TitlePanel() {

        titlePanel.setBackground(ColorsList.TITLE_BACKGROUND_COLOR);
        titlePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        addTitleLabel();

        titlePanel.setVisible(true);
    }

    /**
     * Configura e aggiunge l'etichetta del titolo al pannello.
     * <p>
     * Imposta il font (Segoe UI, Grassetto, 48pt), il colore primario e l'allineamento centrale.
     * </p>
     */
    private void addTitleLabel() {

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(ColorsList.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER);
        titlePanel.add(titleLabel, Constraints.getGridBagConstraints());
    }

    /**
     * Restituisce il pannello grafico da inserire nel frame principale.
     *
     * @return Il {@link JPanel} (specificamente RoundedPanel) contenente il titolo.
     */
    public JPanel getTitlePanel() {
        return titlePanel;
    }

    /**
     * Aggiorna il testo visualizzato nell'intestazione.
     * <p>
     * Questo metodo è il punto di accesso principale per le altre classi della GUI
     * per cambiare contesto visivo (es. quando si passa da "Home" a "Gestione Team").
     * </p>
     *
     * @param title Il nuovo titolo da visualizzare.
     */
    public void setTitle(String title) {

        titleLabel.setText(title);
    }

    /**
     * Metodo di accesso all'istanza Singleton.
     * <p>
     * Implementa la "Lazy Initialization": l'istanza viene creata solo alla prima chiamata.
     * </p>
     *
     * @return L'unica istanza di {@code TitlePanel}.
     */
    public static TitlePanel getInstance() {

        if (instance == null)
            instance = new TitlePanel();

        return instance;
    }
}