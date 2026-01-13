package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

/**
 * Pulsante per avviare la ricerca dei progetti, specifico per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link SearchProjectsButtonUser}.
 * Condivide la stessa gestione dell'input (casella di testo e placeholder) della classe genitore,
 * ma sovrascrive il metodo {@link #search} per indirizzare l'output verso la vista dedicata ai developer.
 * </p>
 */
public class SearchProjectsButtonDeveloper extends SearchProjectsButtonUser {

    /**
     * Costruttore del pulsante di ricerca Developer.
     * <p>
     * Inizializza il pulsante passando i riferimenti necessari alla superclasse.
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home (dove verranno iniettati i risultati).
     * @param searchTextField Il campo di testo da cui leggere la query.
     * @param placeholder     Il testo segnaposto da ignorare.
     */
    public SearchProjectsButtonDeveloper(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super(mainFrame, homePanel, searchTextField, placeholder);
    }

    /**
     * Esegue la logica di ricerca e visualizzazione per lo Sviluppatore.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * </p>
     * <ol>
     * <li>Recupera il termine di ricerca dal campo di testo (gestendo il placeholder).</li>
     * <li>Invoca {@link ProjectController#searchProjectsByName} per cercare i progetti nel database.</li>
     * <li>Se la ricerca ha successo, istanzia {@link SearchProjectResultsDeveloper}.</li>
     * </ol>
     * <p>
     * L'uso di {@code SearchProjectResultsDeveloper} è fondamentale perché genera una tabella dei risultati
     * che include la colonna "Assigned" (Issue Assegnate), funzionalità non presente per l'utente base.
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home.
     * @param searchTextField Il campo di testo.
     * @param placeholder     Il placeholder.
     */
    @Override
    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        // Esegue la query al controller
        boolean success = ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        if(!success)
            return;

        // Visualizza i risultati usando il gestore specifico per Developer (che include la colonna "Assigned")
        new SearchProjectResultsDeveloper(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}