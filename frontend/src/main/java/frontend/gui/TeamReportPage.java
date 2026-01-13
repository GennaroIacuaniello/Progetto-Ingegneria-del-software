package frontend.gui;

import java.awt.*;

/**
 * Pagina principale per la visualizzazione dei Report Mensili dei Team.
 * <p>
 * Questa classe estende {@link RoundedPanel} e fornisce l'interfaccia utente per analizzare
 * le statistiche dei membri di un team.
 * La pagina è divisa verticalmente in due sezioni:
 * </p>
 * <ol>
 * <li><b>Area di Ricerca (Top):</b> Contiene il {@link TeamReportSearchPanel}, che permette di selezionare il team desiderato.</li>
 * <li><b>Area Risultati (Center):</b> Contiene il {@link SearchViewResults}, dove verranno visualizzate le tabelle statistiche (create da {@link ReportResults}).</li>
 * </ol>
 * <p>
 * Gestisce inoltre la navigazione per tornare al pannello di gestione team.
 * </p>
 */
public class TeamReportPage extends RoundedPanel{

    /**
     * Riferimento al pannello di gestione team genitore (usato per la navigazione "Indietro").
     */
    ManageTeamsPanel manageTeamsPanel;

    /**
     * Il contenitore dinamico per i risultati (tabelle) del report.
     */
    SearchViewResults searchViewResults;

    /**
     * Costruttore della pagina di Report.
     * <p>
     * Inizializza il layout, configura lo stile grafico (trasparenza), aggiunge i pannelli
     * di ricerca e risultati, e imposta il titolo globale della schermata su "REPORT MENSILE".
     * </p>
     *
     * @param manageTeamsPanel Il pannello chiamante, necessario per tornare indietro.
     */
    public TeamReportPage(ManageTeamsPanel manageTeamsPanel) {

        super(new GridBagLayout());

        this.manageTeamsPanel = manageTeamsPanel;

        setPanel();
        setTeamReportSearchPanel();
        setSearchViewResults();

        TitlePanel.getInstance().setTitle("REPORT MENSILE");
    }

    /**
     * Configura l'aspetto del pannello contenitore.
     * <p>
     * Rimuove bordi e sfondo (trasparenza) per integrarsi con il design dell'applicazione.
     * </p>
     */
    private void setPanel() {

        this.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        this.setBackground(ColorsList.EMPTY_COLOR);
    }

    /**
     * Inizializza e posiziona il pannello di ricerca.
     * <p>
     * Istanzia {@link TeamReportSearchPanel} passando {@code this} come riferimento,
     * permettendo al pannello di ricerca di richiamare {@link #updateReportViewResults}
     * quando i dati sono pronti.
     * </p>
     */
    private void setTeamReportSearchPanel() {

        TeamReportSearchPanel teamReportSearchPanel = new TeamReportSearchPanel(this);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                32, 32, GridBagConstraints.CENTER, 0.5f, 0.5f,
                new Insets(10, 80, 10, 80));
        this.add(teamReportSearchPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza l'area dei risultati.
     * <p>
     * Imposta un messaggio placeholder iniziale specifico per i report:
     * "Effettua una ricerca per visualizzare i risultati".
     * </p>
     */
    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("<html><center>Effettua una ricerca<br>" +
                "per visualizzare i risultati</html>");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new  Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    /**
     * Aggiorna la vista dei risultati con i dati del report generato.
     * <p>
     * Questo metodo viene invocato dal pannello di ricerca (o dalla classe {@link ReportResults})
     * una volta che le statistiche sono state calcolate.
     * </p>
     *
     * @param component Il componente grafico (solitamente un pannello con tabelle) da mostrare.
     */
    public void updateReportViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    /**
     * Gestisce la navigazione per tornare alla schermata precedente.
     * <p>
     * Invoca il metodo del pannello genitore per ripristinare la visualizzazione della gestione team.
     * </p>
     */
    public void returnToManageTeamsPanel() {

        manageTeamsPanel.returnToManageTeamsPanel();
    }
}