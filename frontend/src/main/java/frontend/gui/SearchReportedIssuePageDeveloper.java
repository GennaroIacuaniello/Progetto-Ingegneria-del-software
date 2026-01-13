package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pagina per la ricerca e visualizzazione delle issue segnalate (Lato Sviluppatore).
 * <p>
 * Questa classe estende {@link SearchReportedIssuePageUser}, ereditando il layout generale
 * (titolo e area dei risultati).
 * La modifica sostanziale avviene nel metodo {@link #setIssueSearchPanel}, dove viene iniettato
 * il pannello di ricerca specifico per sviluppatori. Questo permette di filtrare le issue
 * non solo per Titolo, Stato, Tipo e Tag, ma anche per <b>Priorità</b>, un attributo visibile
 * e filtrabile solo dai tecnici.
 * </p>
 */
public class SearchReportedIssuePageDeveloper extends SearchReportedIssuePageUser {

    /**
     * Costruttore della pagina Developer.
     * <p>
     * Inizializza la pagina richiamando il costruttore della superclasse per il setup del layout base.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home per la gestione della navigazione.
     */
    public SearchReportedIssuePageDeveloper(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    /**
     * Configura e inserisce il pannello dei criteri di ricerca.
     * <p>
     * Sovrascrive il metodo della superclasse per istanziare {@link ReportedIssueSearchPanelDeveloper}
     * anziché la versione utente.
     * Questo cambio sostituisce la barra di ricerca standard con quella che include il filtro "Priorità".
     * </p>
     *
     * @param mainFrame Il frame principale (necessario per i componenti interni del pannello).
     */
    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        // Istanzia il pannello di ricerca avanzato (con filtro Priorità)
        ReportedIssueSearchPanelUser issueSearchPanel = new ReportedIssueSearchPanelDeveloper(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }
}