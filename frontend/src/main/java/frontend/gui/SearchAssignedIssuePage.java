package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pagina per la ricerca e visualizzazione delle issue assegnate allo sviluppatore corrente.
 * <p>
 * Questa classe estende {@link SearchReportedIssuePageDeveloper} ereditandone il layout generale
 * (pannello di ricerca in alto, risultati in basso).
 * La specializzazione avviene sostituendo il pannello di ricerca standard con {@link AssignedIssueSearchPanel},
 * che interroga il backend specificamente per le issue in carico all'utente loggato.
 * </p>
 */
public class SearchAssignedIssuePage extends SearchReportedIssuePageDeveloper{

    /**
     * Costruttore della pagina delle issue assegnate.
     * <p>
     * Inizializza la pagina richiamando il costruttore della superclasse per il setup del layout base.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home per la gestione della navigazione.
     */
    public SearchAssignedIssuePage(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    /**
     * Imposta il titolo della pagina nella barra superiore.
     * <p>
     * Sovrascrive il metodo per indicare il contesto specifico: "ISSUE ASSEGNATE".
     * </p>
     */
    @Override
    protected void setTitlePanel() {

        TitlePanel.getInstance().setTitle("ISSUE ASSEGNATE");
    }

    /**
     * Configura e inserisce il pannello dei criteri di ricerca.
     * <p>
     * Sovrascrive il metodo base per istanziare {@link AssignedIssueSearchPanel}.
     * Questo pannello specifico conterrà la logica per filtrare le issue basandosi
     * sull'assegnatario (l'utente corrente) piuttosto che sull'autore della segnalazione.
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new AssignedIssueSearchPanel(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }
}