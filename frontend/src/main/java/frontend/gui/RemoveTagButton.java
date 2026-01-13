package frontend.gui;

import java.awt.*;

/**
 * Pulsante per la rimozione di un singolo tag dalla lista.
 * <p>
 * Questa classe estende {@link IconButton} e visualizza un'icona a forma di cestino.
 * La sua responsabilità principale è gestire l'evento di cancellazione:
 * rimuove il pannello del tag sia dall'interfaccia grafica che dalla struttura dati interna,
 * e successivamente riorganizza il layout degli elementi rimanenti per riempire lo spazio vuoto.
 * </p>
 */
public class RemoveTagButton extends IconButton{

    /**
     * Costruttore del pulsante di rimozione.
     * <p>
     * Configura il pulsante con l'icona del cestino ("trashButtonIcon.png") e dimensioni 32x32.
     * Aggiunge un listener che, al click, invoca il metodo {@link #removePanel} per eliminare
     * il tag specifico passato come parametro.
     * </p>
     *
     * @param menu     Il menu contenitore (TagsMenu) che gestisce la lista dei tag.
     * @param tagPanel Il pannello specifico del tag (TagPanel) a cui questo pulsante appartiene.
     */
    public RemoveTagButton(TagsMenu menu, TagPanel tagPanel) {

        super("/frontend/gui/images/trashButtonIcon.png", 32 ,32);

        addActionListener(e -> removePanel(menu, tagPanel));
    }

    /**
     * Esegue la logica di rimozione e riorganizzazione del layout.
     * <p>
     * Questo metodo esegue le seguenti operazioni:
     * <ol>
     * <li>Rimuove il componente grafico {@code tagPanel} dal pannello principale.</li>
     * <li>Rimuove l'oggetto dalla lista logica mantenuta in {@code TagsMenu}.</li>
     * <li><b>Riorganizzazione Griglia:</b> Itera su tutti i pannelli successivi a quello eliminato.
     * Per ognuno di essi:
     * <ul>
     * <li>Decrementa il loro indice interno.</li>
     * <li>Li rimuove e li riaggiunge al layout con i nuovi vincoli (Constraints) aggiornati,
     * spostandoli visivamente verso l'alto per chiudere il "buco" lasciato dal tag eliminato.</li>
     * </ul>
     * </li>
     * <li>Forza il ridisegno (revalidate/repaint) del pannello e dello scroll pane per aggiornare la vista.</li>
     * </ol>
     * </p>
     *
     * @param menu     Il menu contenitore.
     * @param tagPanel Il pannello del tag da rimuovere.
     */
    private void removePanel(TagsMenu menu, TagPanel tagPanel) {

        // 1. Rimuovi il pannello grafico dal contenitore
        menu.getTagsPanel().remove(tagPanel);

        // 2. Rimuovi il pannello dalla lista di gestione
        menu.getTagPanels().remove(tagPanel);

        // 3. Aggiusta indici e posizioni dei pannelli successivi
        // Itera partendo dall'indice del pannello rimosso fino alla fine della lista attuale
        for (int i = tagPanel.getIndex(); i < menu.getTagPanels().size(); i++) {

            // Aggiorna l'indice interno del pannello corrente
            menu.getTagPanels().get(i).decrement();

            // Rimuove temporaneamente il pannello per riposizionarlo
            menu.getTagsPanel().remove(menu.getTagPanels().get(i));

            // Aggiorna i vincoli del GridBagLayout per spostare il pannello nella riga precedente (i)
            Constraints.setConstraints(0, i, 1, 1,
                    GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                    new Insets(5, 5, 5, 5));

            // Riaggiunge il pannello con i nuovi vincoli
            menu.getTagsPanel().add(menu.getTagPanels().get(i), Constraints.getGridBagConstraints());
        }

        // 4. Aggiorna l'interfaccia grafica
        menu.getTagsPanel().revalidate();
        menu.getTagsPanel().repaint();

        menu.getScrollPane().revalidate();
        menu.getScrollPane().repaint();
    }
}