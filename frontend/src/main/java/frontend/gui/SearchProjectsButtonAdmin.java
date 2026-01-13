package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

/**
 * Pulsante per avviare la ricerca dei progetti, specifico per il ruolo Amministratore.
 * <p>
 * Questa classe estende {@link SearchProjectsButtonDeveloper}.
 * Mantiene la stessa logica di interazione con l'input utente (gestione del testo e del placeholder),
 * ma sovrascrive il metodo {@link #search} per garantire che i risultati vengano visualizzati
 * utilizzando {@link SearchProjectResultsAdmin}.
 * Questo assicura che la tabella risultante includa le funzionalità esclusive dell'amministratore,
 * come "Gestisci Team" e "Vedi tutte le issue".
 * </p>
 */
public class SearchProjectsButtonAdmin extends SearchProjectsButtonDeveloper {

    /**
     * Costruttore del pulsante di ricerca Admin.
     * <p>
     * Passa tutti i parametri alla superclasse per l'inizializzazione grafica e del listener.
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home (dove verranno iniettati i risultati).
     * @param searchTextField Il campo di testo da cui leggere la query.
     * @param placeholder     Il testo segnaposto da ignorare.
     */
    public SearchProjectsButtonAdmin(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super(mainFrame, homePanel, searchTextField, placeholder);
    }

    /**
     * Esegue la logica di ricerca e visualizzazione per l'Amministratore.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * <ol>
     * <li>Recupera il testo dal campo di input (convertendo il placeholder in stringa vuota).</li>
     * <li>Invoca {@link ProjectController#searchProjectsByName} per eseguire la query sul database.</li>
     * <li>Se la ricerca ha successo, istanzia {@link SearchProjectResultsAdmin}.</li>
     * </ol>
     * L'uso di {@code SearchProjectResultsAdmin} è il punto chiave: trasforma i dati grezzi (ID e Nomi)
     * in una tabella completa di 7 colonne, abilitando le funzioni di gestione.
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home.
     * @param searchTextField Il campo di testo.
     * @param placeholder     Il placeholder.
     */
    @Override
    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        // Esegue la ricerca tramite il controller
        boolean success = ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        if(!success)
            return;

        // Visualizza i risultati usando il gestore specifico per Admin
        new SearchProjectResultsAdmin(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}