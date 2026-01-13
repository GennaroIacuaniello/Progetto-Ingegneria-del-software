package frontend.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello grafico che rappresenta una singola etichetta (Tag) in fase di creazione o modifica.
 * <p>
 * Questa classe estende {@link RoundedPanel} per fornire un contenitore visivo arrotondato per ogni tag.
 * Ogni {@code TagPanel} è composto da due elementi principali:
 * <ol>
 * <li><b>Campo di testo:</b> Per digitare il nome dell'etichetta (es. "Backend", "Urgente").</li>
 * <li><b>Pulsante Rimuovi:</b> Un pulsante (X) per eliminare questa specifica etichetta dalla lista.</li>
 * </ol>
 * La classe mantiene anche il proprio indice posizionale all'interno della lista dei tag gestita da {@link TagsMenu},
 * permettendo una gestione ordinata delle rimozioni.
 * </p>
 */
public class TagPanel extends RoundedPanel{

    /**
     * Campo di input per il testo del tag.
     */
    private JTextField tagField;

    /**
     * L'indice di posizione di questo tag nella lista del menu padre.
     * <br>
     * L'annotazione {@code @Getter} di Lombok genera automaticamente il metodo {@code getIndex()}.
     */
    @Getter
    private int index;

    /**
     * Testo placeholder visualizzato quando il campo è vuoto.
     */
    private static final String TAGFIELD_PLACEHOLDER = "Inserisci etichetta";

    /**
     * Costruttore del pannello Tag.
     * <p>
     * Inizializza il layout, lo stile grafico (sfondo bianco, bordo arrotondato) e aggiunge
     * i componenti interattivi (campo testo e bottone elimina).
     * </p>
     *
     * @param menu  Il menu contenitore {@link TagsMenu} (necessario per gestire la rimozione).
     * @param index La posizione iniziale di questo tag nella lista.
     */
    public TagPanel(TagsMenu menu, int index) {

        super(new GridBagLayout());

        this.index = index;

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.white);

        setField();
        setRemoveTagButton(menu);
    }

    /**
     * Configura e aggiunge il campo di testo per l'inserimento del tag.
     * <p>
     * Imposta il placeholder e rimuove i bordi standard per integrarsi nel design arrotondato del pannello.
     * </p>
     */
    private void setField() {

        tagField = new JTextField(TAGFIELD_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(tagField, TAGFIELD_PLACEHOLDER);

        tagField.setBorder(BorderFactory.createEmptyBorder());
        tagField.setBackground(ColorsList.EMPTY_COLOR);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(tagField, Constraints.getGridBagConstraints());
    }

    /**
     * Configura e aggiunge il pulsante per rimuovere questo tag.
     * <p>
     * Istanzia un {@link RemoveTagButton}, passandogli il riferimento al menu padre e a questo pannello stesso,
     * permettendo al bottone di orchestrare l'eliminazione del componente.
     * </p>
     *
     * @param menu Il menu padre che gestisce la lista dei pannelli.
     */
    private void setRemoveTagButton(TagsMenu menu) {

        RemoveTagButton removetagButton = new RemoveTagButton(menu, this);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(removetagButton, Constraints.getGridBagConstraints());
    }

    /**
     * Decrementa l'indice di posizione.
     * <p>
     * Questo metodo è fondamentale per mantenere la coerenza della lista.
     * Quando un tag precedente a questo viene rimosso, tutti i tag successivi devono
     * scalare di una posizione (indice - 1).
     * </p>
     */
    public void decrement() {

        index--;
    }

    /**
     * Restituisce il testo inserito dall'utente nel campo del tag.
     *
     * @return La stringa contenuta nel JTextField.
     */
    public String getTagField() {
        return tagField.getText();
    }
}