package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.util.Objects;

/**
 * Pannello per la ricerca globale di tutte le segnalazioni (Issue) all'interno del progetto.
 * <p>
 * Questa classe estende {@link ReportedIssueSearchPanelDeveloper}, ereditandone l'intera interfaccia grafica
 * (campi di input per titolo, stato, tag, tipo e priorità). La differenza sostanziale risiede nella logica
 * del pulsante di ricerca (override di {@code searchButtonActionListener}), che qui interroga il controller
 * per ottenere <b>tutte</b> le issue del progetto, non limitandosi a quelle segnalate dall'utente corrente.
 * </p>
 */
public class AllIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    /**
     * Costruttore del pannello.
     * <p>
     * Inizializza i componenti grafici richiamando il costruttore della superclasse.
     * </p>
     *
     * @param mainFrame  Il frame principale dell'applicazione.
     * @param searchPage La pagina contenitore che ospita questo pannello.
     */
    public AllIssueSearchPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);
    }

    /**
     * Definisce l'azione da eseguire al click del pulsante "Cerca".
     * <p>
     * Raccoglie i valori inseriti nei filtri (titolo, stato, tag, tipo, priorità),
     * gestendo i casi di campi vuoti o valori di default (placeholder).
     * Invoca il metodo {@link IssueController#searchAllIssues} per recuperare le segnalazioni corrispondenti.
     * Se la ricerca ha successo, istanzia e visualizza il pannello dei risultati {@link AllIssueSearchResultsPanel}.
     * </p>
     *
     * @param mainFrame Il frame principale su cui aggiornare la vista con i risultati.
     */
    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        boolean success = IssueController.getInstance().searchAllIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())), tagsButton.getTags(), formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem());

        if(!success)
            return;

        new AllIssueSearchResultsPanel(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}