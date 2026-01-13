package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello di ricerca progetti specifico per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link SearchProjectPanelUser}. Eredita interamente la struttura visiva
 * (container arrotondato, campo di testo, layout), ma personalizza il componente di azione (il pulsante).
 * Sostituisce il pulsante di ricerca standard con {@link SearchProjectsButtonDeveloper}, garantendo che
 * la tabella dei risultati generata includa le funzionalità extra necessarie agli sviluppatori.
 * </p>
 */
public class SearchProjectPanelDeveloper extends SearchProjectPanelUser {

    /**
     * Costruttore del pannello di ricerca Developer.
     * <p>
     * Inizializza il pannello richiamando il costruttore della superclasse per configurare
     * il layout di base e il campo di testo.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home dello sviluppatore (necessario per il refresh della vista).
     */
    public SearchProjectPanelDeveloper(JFrame mainFrame, HomePanelDeveloper homePanel) {

        super(mainFrame, homePanel);
    }

    /**
     * Configura e aggiunge il pulsante di ricerca al pannello.
     * <p>
     * Sovrascrive il metodo della superclasse per istanziare {@link SearchProjectsButtonDeveloper}.
     * Questo è il punto chiave dell'estensione: utilizzando questo pulsante specifico,
     * l'azione di ricerca istanzierà un {@link ProjectTableModelDeveloper} invece di quello base,
     * abilitando colonne e azioni aggiuntive nei risultati.
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello Home.
     */
    @Override
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        // Istanzia il pulsante con logica Developer
        SearchProjectsButtonUser searchButton = new SearchProjectsButtonDeveloper(mainFrame, homePanel, searchTextField, TEXTFIELD_PLACEHOLDER);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        searchProjectPanel.add(searchButton, Constraints.getGridBagConstraints());
    }
}