package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pagina per la ricerca e visualizzazione globale di tutte le issue (Vista Amministratore).
 * <p>
 * Questa classe estende {@link SearchAssignedIssuePage} ereditandone la struttura grafica di base
 * (layout a due pannelli: criteri di ricerca in alto, risultati in basso).
 * <br>
 * La differenza fondamentale risiede nel componente di ricerca: utilizza {@link AllIssueSearchPanel},
 * che permette di cercare tra tutte le issue del progetto senza i filtri restrittivi
 * (come "solo le mie segnalazioni" o "solo le mie assegnazioni") tipici delle viste utente/sviluppatore.
 * </p>
 */
public class SearchAllIssuePage extends SearchAssignedIssuePage{

    /**
     * Costruttore della pagina di ricerca globale.
     * <p>
     * Inizializza la pagina richiamando il costruttore della superclasse per il setup del layout.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param homePanel Il pannello Home per la gestione della navigazione.
     */
    public SearchAllIssuePage(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    /**
     * Imposta il titolo della pagina nella barra superiore.
     * <p>
     * Sovrascrive il metodo per indicare che si sta visualizzando l'elenco completo: "VEDI TUTTE LE ISSUE".
     * </p>
     */
    @Override
    protected void setTitlePanel() {

        TitlePanel.getInstance().setTitle("VEDI TUTTE LE ISSUE");
    }

    /**
     * Configura e inserisce il pannello dei criteri di ricerca.
     * <p>
     * Sovrascrive il metodo base per istanziare specificamente {@link AllIssueSearchPanel}.
     * Questo pannello fornirà i filtri necessari per una ricerca ad ampio spettro su tutto il progetto
     * (es. cercare issue assegnate ad altri sviluppatori).
     * </p>
     *
     * @param mainFrame Il frame principale (necessario per i dialoghi dei filtri).
     */
    @Override
    protected void setIssueSearchPanel(JFrame mainFrame) {

        // Istanzia il pannello di ricerca "Globale"
        ReportedIssueSearchPanelUser issueSearchPanel = new AllIssueSearchPanel(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }
}