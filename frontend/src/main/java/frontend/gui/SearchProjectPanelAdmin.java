package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello di ricerca progetti specifico per il ruolo Amministratore.
 * <p>
 * Questa classe estende {@link SearchProjectPanelDeveloper}. Eredita l'intera struttura grafica
 * (campo di testo, layout arrotondato), ma interviene sulla logica di attivazione della ricerca.
 * Il suo scopo principale è iniettare il pulsante {@link SearchProjectsButtonAdmin}, il quale,
 * quando premuto, genererà una tabella dei risultati contenente le colonne e le azioni
 * esclusive per gli admin (come "Gestisci Team" o "Vedi tutte le issue").
 * </p>
 */
public class SearchProjectPanelAdmin extends SearchProjectPanelDeveloper{

    /**
     * Costruttore del pannello di ricerca Admin.
     * <p>
     * Inizializza il pannello richiamando il costruttore della superclasse per il setup grafico di base.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home dell'admin.
     */
    public SearchProjectPanelAdmin(JFrame mainFrame, HomePanelAdmin homePanel) {

        super(mainFrame, homePanel);
    }

    /**
     * Configura e aggiunge il pulsante di ricerca al pannello.
     * <p>
     * Sovrascrive il metodo della superclasse per istanziare {@link SearchProjectsButtonAdmin}
     * invece del pulsante generico o developer.
     * Questo è il punto di divergenza cruciale: questo pulsante sa come creare il
     * {@link ProjectTableModelAdmin} per visualizzare i risultati con i permessi elevati.
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello Home (necessario per aggiornare la vista centrale con i risultati).
     */
    @Override
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        // Istanzia il pulsante con logica Admin
        SearchProjectsButtonUser searchButton = new SearchProjectsButtonAdmin(mainFrame, homePanel, searchTextField, TEXTFIELD_PLACEHOLDER);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        searchProjectPanel.add(searchButton, Constraints.getGridBagConstraints());
    }
}