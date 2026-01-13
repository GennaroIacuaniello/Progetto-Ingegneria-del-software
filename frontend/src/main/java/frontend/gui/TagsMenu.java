package frontend.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Finestra di dialogo per la gestione della lista dei Tag.
 * <p>
 * Questa classe estende {@link MyDialog} e fornisce l'interfaccia grafica per aggiungere
 * dinamicamente molteplici etichette (tag) a una segnalazione.
 * <br>
 * Struttura:
 * <ul>
 * <li>Un pulsante "Aggiungi" (+) in alto a destra.</li>
 * <li>Un'area scorrevole ({@link JScrollPane}) che contiene la lista dei pannelli.</li>
 * <li>Una lista di {@link TagPanel}, ognuno rappresentante una singola etichetta modificabile.</li>
 * </ul>
 * <b>Nota:</b> La classe è annotata con {@code @Getter} per esporre la lista {@code tagPanels},
 * permettendo a {@link TagsButton} di recuperare i testi inseriti dall'utente.
 * </p>
 */
@Getter
public class TagsMenu extends MyDialog{

    /**
     * Lista che mantiene i riferimenti ai pannelli dei tag creati.
     * Necessaria per iterare sui componenti e recuperare il testo inserito in un secondo momento.
     */
    private final ArrayList<TagPanel> tagPanels = new ArrayList<>();

    private JScrollPane scrollPane;
    private JPanel tagsPanel;

    /**
     * Costruttore del menu dei Tag.
     * <p>
     * Configura le dimensioni della finestra (200x400) e inizializza i componenti grafici:
     * il pannello contenitore, lo scroll pane e il pulsante di aggiunta.
     * </p>
     *
     * @param owner Il frame principale che possiede questa finestra di dialogo.
     */
    public TagsMenu(JFrame owner) {

        super(owner);

        mainPanel.setMinimumSize(new Dimension(200, 400));
        mainPanel.setPreferredSize(new Dimension(200, 400));

        setTagsPanel();
        setScrollPane();
        setAddButton();

        pack();
    }

    /**
     * Inizializza il pannello interno che conterrà fisicamente i TagPanel.
     * <p>
     * Usa un {@link GridBagLayout} per impilare i tag verticalmente in modo ordinato.
     * </p>
     */
    private void setTagsPanel() {

        tagsPanel = new JPanel(new GridBagLayout());

        tagsPanel.setBorder(BorderFactory.createEmptyBorder());
        tagsPanel.setBackground(ColorsList.EMPTY_COLOR);
    }

    /**
     * Configura l'area scorrevole.
     * <p>
     * Avvolge {@code tagsPanel} in uno {@link JScrollPane} per permettere l'inserimento
     * di un numero indefinito di tag senza rompere il layout della finestra.
     * Configura inoltre la trasparenza per mantenere coerenza stilistica.
     * </p>
     */
    private void setScrollPane() {

        scrollPane = new JScrollPane();

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        scrollPane.setViewportView(tagsPanel);

        scrollPane.setPreferredSize(new Dimension(200, 350));

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.PAGE_START,
                1f, 1f, new Insets(5, 5, 5, 5));
        mainPanel.add(scrollPane, Constraints.getGridBagConstraints());
    }

    /**
     * Aggiunge il pulsante "+" per creare nuovi tag.
     * <p>
     * Posizionato in alto a destra. Al click, invoca {@link #addTagPanel()}.
     * </p>
     */
    private void setAddButton() {

        IconButton addButton = new IconButton("/frontend/gui/images/addButtonIcon.svg", 32, 32);

        addButton.addActionListener(e -> addTagPanel());

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(5, 5, 5, 5));
        mainPanel.add(addButton, Constraints.getGridBagConstraints());
    }

    /**
     * Aggiunge logicamente e graficamente un nuovo tag alla lista.
     * <p>
     * 1. Crea una nuova istanza di {@link TagPanel}, passandogli l'indice corrente.<br>
     * 2. Aggiunge il pannello alla lista logica {@code tagPanels}.<br>
     * 3. Aggiunge il pannello al contenitore grafico {@code tagsPanel} usando i vincoli del GridBagLayout.<br>
     * 4. Forza il ricalcolo del layout (revalidate/repaint) per mostrare immediatamente il nuovo campo.
     * </p>
     */
    private void addTagPanel() {

        tagPanels.add(new TagPanel(this, tagPanels.size()));

        Constraints.setConstraints(0, tagPanels.size() - 1, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        tagsPanel.add(tagPanels.getLast(), Constraints.getGridBagConstraints());

        tagsPanel.revalidate();
        tagsPanel.repaint();

        scrollPane.revalidate();
        scrollPane.repaint();
    }

    /**
     * Gestisce l'azione del pulsante "Indietro" o chiusura.
     * <p>
     * <b>Importante:</b> Sovrascrive il comportamento standard per usare {@code setVisible(false)}
     * invece di distruggere l'oggetto. Questo preserva lo stato della finestra (i tag inseriti)
     * permettendo all'utente di chiudere il menu, tornare alla segnalazione, e riaprire il menu
     * ritrovando i tag precedentemente aggiunti.
     * </p>
     */
    @Override
    protected void backActionListener() {

        setVisible(false);
    }
}