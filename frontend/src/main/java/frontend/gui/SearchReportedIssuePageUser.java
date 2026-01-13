package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pagina principale per la ricerca e visualizzazione delle issue segnalate (Lato Utente).
 * <p>
 * Questa classe estende {@link RoundedPanel} e agisce come contenitore macroscopico.
 * Il layout è diviso verticalmente in due sezioni:
 * </p>
 * <ol>
 * <li><b>Area Superiore:</b> Contiene il pannello di ricerca {@link ReportedIssueSearchPanelUser} con i filtri.</li>
 * <li><b>Area Inferiore:</b> Contiene il {@link SearchViewResults}, un'area scorrevole dove viene renderizzata la tabella dei risultati.</li>
 * </ol>
 * <p>
 * Questa classe è progettata per essere estesa (es. da {@link SearchReportedIssuePageDeveloper}) per modificare
 * il tipo di pannello di ricerca o il titolo della pagina, mantenendo inalterata la struttura del layout.
 * </p>
 */
public class SearchReportedIssuePageUser extends RoundedPanel{

    /**
     * Wrapper per l'area dei risultati (contiene essenzialmente uno JScrollPane).
     */
    private SearchViewResults searchViewResults;

    /**
     * Riferimento al pannello Home per gestire la navigazione (pulsante "Indietro").
     */
    private final HomePanelUser homePanelUser;

    /**
     * Costruttore della pagina.
     * <p>
     * Imposta il layout (GridBagLayout), configura lo stile grafico e assembla i componenti
     * richiamando i metodi di setup in sequenza.
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello Home.
     */
    public SearchReportedIssuePageUser(JFrame mainFrame, HomePanelUser homePanel) {

        super(new GridBagLayout());

        this.homePanelUser = homePanel;

        setPanel();
        setIssueSearchPanel(mainFrame);
        setIssueSearchResultsPanel();
        setTitlePanel();
    }

    /**
     * Imposta il titolo della pagina nella barra superiore globale.
     * <p>
     * Testo di default: "ISSUE REPORTATE".
     * Questo metodo è {@code protected} per permettere alle sottoclassi di cambiare il titolo
     * (es. "ISSUE ASSEGNATE" o "VEDI TUTTE LE ISSUE").
     * </p>
     */
    protected void setTitlePanel() {

        TitlePanel.getInstance().setTitle("ISSUE REPORTATE");
    }

    /**
     * Configura l'aspetto del pannello contenitore (trasparente).
     */
    private void setPanel() {

        this.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        this.setBackground(ColorsList.EMPTY_COLOR);
    }

    /**
     * Inizializza e aggiunge il pannello dei criteri di ricerca (Filtri).
     * <p>
     * Nella versione Utente, istanzia {@link ReportedIssueSearchPanelUser}.
     * Le sottoclassi (Developer/Admin) sovrascriveranno questo metodo per iniettare
     * i loro pannelli di ricerca specifici (che includono filtri extra come la Priorità).
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    protected void setIssueSearchPanel(JFrame mainFrame) {

        ReportedIssueSearchPanelUser issueSearchPanel = new ReportedIssueSearchPanelUser(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 60, 10, 60));
        this.add(issueSearchPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza l'area dedicata ai risultati.
     * <p>
     * Crea un'istanza di {@link SearchViewResults} che inizialmente è vuota, ma pronta
     * a ricevere una JTable quando verrà effettuata una ricerca.
     * </p>
     */
    protected void setIssueSearchResultsPanel() {

        searchViewResults = new SearchViewResults();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new  Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    /**
     * Aggiorna l'area dei risultati con una nuova tabella.
     * <p>
     * Questo metodo viene chiamato dai pannelli di ricerca (es. {@code ReportedIssueSearchResultsPanelUser})
     * quando una query al database restituisce dei dati. Sostituisce il contenuto corrente
     * dello scroll pane con il nuovo componente (la tabella).
     * </p>
     *
     * @param component Il componente (solitamente una JTable) da visualizzare.
     */
    public void updateSearchIssueViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    /**
     * Metodo helper per la navigazione.
     * <p>
     * Permette ai componenti interni (come il pulsante "Indietro" nel pannello di ricerca)
     * di riportare l'utente alla schermata di default del pannello Home.
     * </p>
     */
    public void homePanelReturnToDefaultContentPane() {

        homePanelUser.returnToDefaultContentPanel();
    }
}