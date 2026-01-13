package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

/**
 * Pulsante per avviare la ricerca dei progetti (Lato Utente Standard).
 * <p>
 * Questa classe estende {@link IconButton} e rappresenta la lente d'ingrandimento nella barra di ricerca.
 * Oltre a gestire l'aspetto grafico, si occupa di:
 * </p>
 * <ol>
 * <li>Ascoltare gli eventi di attivazione (Click del mouse o Tasto Invio nel campo di testo).</li>
 * <li>Invocare il controller per eseguire la ricerca nel database.</li>
 * <li>Aggiornare l'interfaccia mostrando i risultati tramite {@link SearchProjectResultsUser}.</li>
 * </ol>
 * <p>
 * Questa classe è progettata per essere estesa da {@link SearchProjectsButtonDeveloper} e
 * {@link SearchProjectsButtonAdmin}, che sovrascriveranno il metodo {@code search} per mostrare
 * risultati con funzionalità aggiuntive.
 * </p>
 */
public class SearchProjectsButtonUser extends IconButton {

    /**
     * Costruttore del pulsante di ricerca.
     * <p>
     * Inizializza il pulsante con l'icona "searchButton.svg" e dimensioni 32x32.
     * Richiama immediatamente {@link #setActionListener} per configurare i comportamenti.
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home (necessario per aggiornare la vista centrale).
     * @param searchTextField Il campo di testo associato da cui leggere la query.
     * @param placeholder     Il testo segnaposto (placeholder) da ignorare durante la ricerca.
     */
    public SearchProjectsButtonUser(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super("/frontend/gui/images/searchButton.svg", 32, 32);

        setActionListener(mainFrame, homePanel, searchTextField, placeholder);
    }

    /**
     * Configura i listener per l'attivazione della ricerca.
     * <p>
     * Associa l'azione di ricerca (metodo {@link #search}) a due eventi distinti:
     * </p>
     * <ol>
     * <li>Click su questo pulsante.</li>
     * <li>Pressione del tasto <b>Invio</b> mentre il focus è sul campo di testo ({@code searchTextField}).</li>
     * </ol>
     * <p>
     * Questo migliora l'usabilità permettendo di cercare senza dover cliccare col mouse.
     * </p>
     *
     * @param mainFrame       Il frame principale dell'applicazione.
     * @param homePanel       Il pannello Home (necessario per aggiornare la vista risultati).
     * @param searchTextField Il campo di testo su cui attivare il listener per il tasto Invio.
     * @param placeholder     Il testo segnaposto da passare alla logica di ricerca.
     */
    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        this.addActionListener(e -> search(mainFrame, homePanel, searchTextField, placeholder));

        // Permette di cercare premendo "Invio" nel campo di testo
        searchTextField.addActionListener(e -> search(mainFrame, homePanel, searchTextField, placeholder));
    }

    /**
     * Esegue la logica di ricerca e aggiornamento della vista.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * </p>
     * <ol>
     * <li><b>Input Cleaning:</b> Legge il testo dal campo. Se il testo è uguale al placeholder (es. "Inserire nome progetto"),
     * lo converte in una stringa vuota per cercare tutti i progetti.</li>
     * <li><b>Controller Call:</b> Invoca {@link ProjectController#searchProjectsByName}.</li>
     * <li><b>View Update:</b> Se la ricerca ha successo, istanzia {@link SearchProjectResultsUser}.
     * Questo sostituisce il contenuto del pannello Home con la tabella dei risultati.</li>
     * </ol>
     * <p>
     * <b>Nota:</b> Questo metodo è {@code protected} per permettere alle sottoclassi di sovrascriverlo
     * e istanziare visualizzatori di risultati più avanzati (es. per Developer o Admin).
     * </p>
     *
     * @param mainFrame       Il frame principale.
     * @param homePanel       Il pannello Home.
     * @param searchTextField Il campo di testo.
     * @param placeholder     Il placeholder.
     */
    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        // Se il testo è il placeholder, cerca stringa vuota (""), altrimenti cerca il testo inserito
        boolean success = ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        if(!success)
            return;

        // Visualizza i risultati usando il gestore base per User
        new SearchProjectResultsUser(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}