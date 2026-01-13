package frontend.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pulsante grafico per l'apertura e la gestione del menu dei Tag.
 * <p>
 * Questa classe estende {@link IconButton} e rappresenta il componente visibile nel form
 * (l'icona del cartellino).
 * <br>
 * Le sue responsabilità principali sono due:
 * </p>
 * <ol>
 * <li><b>Interazione UI:</b> Al click, apre e mostra la finestra {@link TagsMenu} sovrapposta al frame principale.</li>
 * <li><b>Recupero Dati:</b> Fornisce il metodo {@link #getTags()} che permette ai form genitori (es. {@link ReportIssueUser})
 * di estrarre la lista delle stringhe (tag) create dall'utente all'interno del menu, per poi inviarle al Controller.</li>
 * </ol>
 */
public class TagsButton extends IconButton {

    /**
     * Riferimento alla finestra/menu che gestisce la lista dei tag.
     * Viene mantenuta come variabile d'istanza per poterne recuperare i dati successivamente.
     */
    private final TagsMenu tagsMenu;

    /**
     * Costruttore del pulsante Tag.
     * <p>
     * 1. Inizializza il pulsante con l'icona specifica ("tagsButton.svg").<br>
     * 2. Istanzia il {@link TagsMenu}, collegandolo al frame principale.<br>
     * 3. Imposta la posizione del menu e lo nasconde inizialmente (sarà mostrato solo al click).<br>
     * 4. Configura il listener per l'apertura.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione (necessario per posizionare il menu modale).
     */
    public TagsButton(JFrame mainFrame) {

        super("/frontend/gui/images/tagsButton.svg", 40, 40);

        tagsMenu = new TagsMenu(mainFrame);
        tagsMenu.setLocationRelativeTo(mainFrame);
        tagsMenu.setVisible(false);

        setActionListener(mainFrame);
    }

    /**
     * Configura l'azione al click del pulsante.
     * <p>
     * Quando l'utente preme il pulsante:
     * 1. Ricalcola la posizione del menu per centrarlo rispetto al frame (utile se il frame è stato spostato).
     * 2. Rende visibile il {@code tagsMenu}.
     * </p>
     */
    private void setActionListener(JFrame mainFrame) {

        this.addActionListener(e -> {

            tagsMenu.setLocationRelativeTo(mainFrame);
            tagsMenu.setVisible(true);
        });
    }

    /**
     * Estrae la lista dei tag inseriti dall'utente.
     * <p>
     * Questo metodo funge da "ponte" tra il form di segnalazione e il menu dei tag.
     * 1. Ottiene la lista dei componenti grafici {@link TagPanel} dal menu.<br>
     * 2. Itera su di essi estraendo il testo contenuto in ciascun campo.<br>
     * 3. Restituisce una lista pulita di stringhe pronte per essere inviate al backend.
     * </p>
     *
     * @return Una {@code List<String>} contenente i testi di tutti i tag validi inseriti.
     */
    public List<String> getTags() {

        List<TagPanel> tagPanels = tagsMenu.getTagPanels();
        List<String> tags = new ArrayList<>();

        for (TagPanel tagPanel : tagPanels) {
            tags.add(tagPanel.getTagField());
        }

        return tags;
    }
}