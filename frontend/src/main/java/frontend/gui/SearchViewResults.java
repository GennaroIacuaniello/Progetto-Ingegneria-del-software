package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Gestore dell'area di visualizzazione (Viewport) dei risultati di ricerca.
 * <p>
 * Questa classe incapsula uno {@link JScrollPane} che funge da contenitore dinamico.
 * Gestisce due stati visivi:
 * <ol>
 * <li><b>Stato Iniziale (Placeholder):</b> Quando non è stata ancora effettuata una ricerca, mostra un
 * pannello informativo (esteticamente coerente con {@link RoundedPanel}) che invita l'utente all'azione.</li>
 * <li><b>Stato Risultati:</b> Quando una ricerca va a buon fine, il contenuto viene sostituito dinamicamente
 * con il componente dei risultati (solitamente una {@link JTable}).</li>
 * </ol>
 * </p>
 */
public class SearchViewResults {

    /**
     * Il componente scrollabile principale.
     */
    private final JScrollPane scrollPane;

    /**
     * Messaggio HTML di default mostrato prima della ricerca.
     */
    private static final String TMPPANEL_PLACEHOLDER = "<html><center>Effettua una ricerca<br>" +
            "per visualizzare i risultati<br>" +
            "e le azioni disponibili</center></html>";

    /**
     * Costruttore standard.
     * <p>
     * Inizializza lo scroll pane e imposta la vista iniziale con il messaggio di default.
     * </p>
     */
    public SearchViewResults() {

        scrollPane = new JScrollPane();

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.setViewportView(createTmpViewPanel(TMPPANEL_PLACEHOLDER));
    }

    /**
     * Costruttore con messaggio personalizzato.
     * <p>
     * Utile se si vuole riutilizzare questo componente in contesti diversi dalla ricerca issue/progetti
     * e si vuole mostrare un testo diverso (es. "Seleziona un team per i dettagli").
     * </p>
     *
     * @param placeHolder Il testo (o HTML) da mostrare nello stato iniziale.
     */
    public SearchViewResults(String placeHolder) {

        scrollPane = new JScrollPane();

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.setViewportView(createTmpViewPanel(placeHolder));
    }

    /**
     * Crea il pannello grafico per il placeholder.
     * <p>
     * Costruisce un {@link RoundedPanel} bianco con bordo colorato che contiene il messaggio.
     * Questo assicura che anche il messaggio di "vuoto" sia stilisticamente gradevole.
     * </p>
     *
     * @param placeHolder Il testo da visualizzare.
     * @return Il pannello configurato.
     */
    private JPanel createTmpViewPanel(String placeHolder) {

        RoundedPanel tmpViewPanel = new RoundedPanel(new GridBagLayout());

        tmpViewPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpViewPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER,
                new Insets(10, 10, 10, 10));
        tmpViewPanel.add(createTmpViewLabel(placeHolder), Constraints.getGridBagConstraints());

        return tmpViewPanel;
    }

    /**
     * Crea l'etichetta di testo per il placeholder.
     * <p>
     * Configura font (Monospaced), allineamento centrale e supporto HTML.
     * </p>
     */
    private JLabel createTmpViewLabel(String placeHolder) {

        JLabel tmpViewLabel = new JLabel(placeHolder);

        tmpViewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tmpViewLabel.setVerticalAlignment(SwingConstants.CENTER);

        tmpViewLabel.setBackground(ColorsList.EMPTY_COLOR);

        tmpViewLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));

        return tmpViewLabel;
    }

    /**
     * Aggiorna il contenuto visualizzato nello scroll pane.
     * <p>
     * Questo è il metodo chiave per passare dallo stato "Placeholder" allo stato "Risultati".
     * Sostituisce la vista corrente (viewport view) con il nuovo componente passato (es. la tabella dei risultati)
     * e forza il ridisegno dell'interfaccia.
     * </p>
     *
     * @param component Il nuovo componente da mostrare (solitamente una JTable o un JPanel contenente tabelle).
     */
    public void updateViewportView (Component component) {

        scrollPane.setViewportView(component);

        scrollPane.revalidate();
        scrollPane.repaint();
    }

    /**
     * Restituisce lo scroll pane gestito.
     * <p>
     * Necessario per aggiungere questo componente al layout della pagina genitore.
     * </p>
     *
     * @return L'istanza di JScrollPane.
     */
    public JScrollPane getViewportScrollPane() {
        return scrollPane;
    }
}

//todo
    /*
        deve contenere un JScrollPane che mostra la tabella costruita in SearchProjectResults,
        a inizio programma si potrebbe visualizzare un messaggio che suggerisce all'utente di effettuare una ricerca
        per visualizzare i risultati. Il JScrollPane deve essere contenuto in HomePanel della classe HomePanel.
     */