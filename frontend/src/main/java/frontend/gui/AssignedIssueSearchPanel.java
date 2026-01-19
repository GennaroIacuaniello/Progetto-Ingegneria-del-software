package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.util.Objects;

/**
 * Pannello per la ricerca delle segnalazioni (Issue) assegnate all'utente corrente (Sviluppatore).
 * <p>
 * Questa classe estende {@link ReportedIssueSearchPanelDeveloper}, ereditando l'interfaccia grafica completa
 * per l'inserimento dei filtri di ricerca (titolo, stato, tag, tipo, priorità).
 * La logica di ricerca viene sovrascritta per invocare il metodo specifico {@link IssueController#searchAssignedIssues},
 * che filtra le issue in base all'assegnatario (l'utente loggato).
 * </p>
 */
public class AssignedIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    /**
     * Costruttore del pannello.
     * <p>
     * Inizializza i componenti grafici chiamando il costruttore della superclasse.
     * </p>
     *
     * @param mainFrame  Il frame principale dell'applicazione.
     * @param searchPage La pagina contenitore che ospita questo pannello.
     */
    public AssignedIssueSearchPanel(JFrame mainFrame,  SearchReportedIssuePageUser searchPage) {
        super(mainFrame, searchPage);
    }

    /**
     * Definisce l'azione da eseguire al click del pulsante "Cerca".
     * <p>
     * Raccoglie i dati dai campi di input, gestendo i valori di default/placeholder.
     * Esegue la ricerca tramite {@link IssueController#searchAssignedIssues}, che restituisce
     * solo le issue assegnate allo sviluppatore corrente che soddisfano i criteri.
     * Se la ricerca ha successo, mostra i risultati nel pannello {@link AssignedIssueSearchResultsPanel}.
     * </p>
     *
     * @param mainFrame Il frame principale su cui aggiornare la vista con i risultati.
     */
    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        boolean success = IssueController.getInstance().searchAssignedIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())), tagsButton.getTags(), formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem());

        if(!success)
            return;

        if (Objects.equals(orderComboBox.getSelectedItem(), "Crescente"))
            IssueController.getInstance().reOrderIssues();

        new AssignedIssueSearchResultsPanel(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}