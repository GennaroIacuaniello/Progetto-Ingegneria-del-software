package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra di dialogo per la visualizzazione della Dashboard di progetto.
 * <p>
 * Questa classe estende {@link MyDialog} e funge da contenitore principale per le statistiche
 * e i grafici del progetto. Al momento dell'inizializzazione, richiede al {@link ProjectController}
 * di recuperare i dati aggiornati dal backend. Se il recupero ha successo, predispone l'area di
 * visualizzazione (viewport) e delega a {@link ReportResults} la generazione e l'inserimento
 * dei componenti grafici specifici (tabelle, istogrammi, ecc.).
 * </p>
 */
public class DashBoard extends MyDialog {

    /**
     * Componente personalizzato che gestisce l'area di visualizzazione scorrevole (viewport)
     * dove vengono mostrati i risultati della dashboard.
     */
    SearchViewResults searchViewResults;

    /**
     * Pannello principale con bordi arrotondati (campo protetto accessibile alle sottoclassi).
     */
    protected RoundedPanel mainPanel;

    /**
     * Costruttore della Dashboard.
     * <p>
     * Esegue le seguenti operazioni:
     * </p>
     * <ol>
     * <li>Invoca il costruttore della superclasse.</li>
     * <li>Chiama {@link ProjectController#createDashBoard()} per recuperare i dati statistici.</li>
     * <li>Se i dati non sono disponibili (es. errore backend), interrompe l'inizializzazione.</li>
     * <li>Configura l'area di visualizzazione tramite {@link #setSearchViewResults()}.</li>
     * <li>Istanzia {@link ReportResults} passandogli un riferimento a se stessa, avviando così la popolazione dei grafici.</li>
     * </ol>
     *
     * @param parent Il frame principale dell'applicazione (parent window).
     */
    public DashBoard(JFrame parent) {

        super(parent);

        boolean success = ProjectController.getInstance().createDashBoard();

        if(!success)
            return;

        setSearchViewResults();

        // Avvia la generazione dei report grafici all'interno di questa dashboard
        new ReportResults(this);
    }

    /**
     * Inizializza e posiziona il contenitore dei risultati.
     * <p>
     * Crea un'istanza di {@link SearchViewResults} e la aggiunge al layout della finestra.
     * Vengono applicati vincoli specifici (Constraints) con ampi margini laterali (Insets 180)
     * per centrare visivamente il contenuto della dashboard.
     * </p>
     */
    private void setSearchViewResults() {

        searchViewResults = new SearchViewResults("");

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 180, 10, 180));
        this.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    /**
     * Aggiorna il contenuto visualizzato nell'area principale della dashboard.
     * <p>
     * Questo metodo è pubblico per permettere a classi esterne (in particolare {@link ReportResults})
     * di iniettare il pannello contenente i grafici e le statistiche generati all'interno dello scroll pane.
     * </p>
     *
     * @param component Il componente grafico (solitamente un pannello con i grafici) da mostrare.
     */
    public void setDashBoardViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }
}