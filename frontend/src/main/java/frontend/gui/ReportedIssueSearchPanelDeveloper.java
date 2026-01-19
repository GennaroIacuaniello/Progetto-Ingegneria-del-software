package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Pannello di ricerca delle segnalazioni (Reported Issues) specifico per gli Sviluppatori.
 * <p>
 * Questa classe estende {@link ReportedIssueSearchPanelUser}, ereditando i filtri base
 * (Titolo, Stato, Tipo, Tag). A differenza della versione utente, questo pannello aggiunge
 * un menu a tendina per filtrare le issue in base alla **Priorità** (es. Alta, Media, Bassa),
 * permettendo allo sviluppatore di organizzare meglio il proprio lavoro.
 * Inoltre, reindirizza i risultati verso un pannello specifico per sviluppatori.
 * </p>
 */
public class ReportedIssueSearchPanelDeveloper extends ReportedIssueSearchPanelUser {

    /**
     * Menu a tendina per la selezione della priorità.
     */
    protected JComboBox<String> priorityComboBox;

    protected JComboBox<String> orderComboBox;

    /**
     * Opzioni disponibili per il filtro priorità.
     */
    private static final String[] priorityOptions = {"Tutte", "Molto bassa", "Bassa", "Media", "Alta", "Molto alta"};

    private static final String[] orderOptions = {"Crescente", "Decrescente"};

    /**
     * Costruttore del pannello di ricerca Developer.
     * <p>
     * Inizializza i componenti base chiamando il costruttore della superclasse e successivamente
     * aggiunge il componente specifico per la selezione della priorità.
     * </p>
     *
     * @param mainFrame  Il frame principale dell'applicazione.
     * @param searchPage La pagina contenitore che ospita questo pannello.
     */
    public ReportedIssueSearchPanelDeveloper(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);

        setPriorityComboBox();
        setOrderComboBox();
    }

    /**
     * Inizializza e posiziona il JComboBox per la priorità.
     * <p>
     * Crea il menu a tendina con le opzioni definite, lo stilizza (rimuovendo bordi standard)
     * e lo inserisce in un contenitore arrotondato tramite {@link ContainerFactory}.
     * Infine, lo aggiunge al layout GridBagLayout in una posizione specifica (colonne 5 e 6),
     * accanto agli altri filtri ereditati.
     * </p>
     */
    public void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(priorityOptions);

        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        // Avvolge il combobox in un pannello arrotondato per coerenza stilistica
        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        // Aggiunge l'etichetta "Priorità:"
        Constraints.setConstraints(5, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Priorità: "), Constraints.getGridBagConstraints());

        // Aggiunge il componente di selezione
        Constraints.setConstraints(6, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    public void setOrderComboBox() {

        orderComboBox = new JComboBox<>(orderOptions);

        orderComboBox.setBorder(BorderFactory.createEmptyBorder());
        orderComboBox.setBackground(ColorsList.EMPTY_COLOR);

        // Avvolge il combobox in un pannello arrotondato per coerenza stilistica
        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(orderComboBox);

        // Aggiunge l'etichetta "Priorità:"
        Constraints.setConstraints(7, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Ordine di priorità: "), Constraints.getGridBagConstraints());

        // Aggiunge il componente di selezione
        Constraints.setConstraints(8, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Gestisce l'azione di ricerca (click sul pulsante "Cerca").
     * <p>
     * Sovrascrive il metodo della superclasse per includere il filtro della priorità nella richiesta.
     * 1. Raccoglie tutti i dati dai campi (Titolo, Stato, Tag, Tipo) e aggiunge la Priorità selezionata.
     * 2. Chiama {@link IssueController#searchReportedIssues} con tutti i parametri.
     * 3. Se la ricerca ha successo, istanzia {@link ReportedIssueSearchResultsPanelDeveloper} per mostrare i risultati.
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        // Esegue la ricerca includendo il parametro priorità (passando null se selezionato "Tutte")
        boolean success = IssueController.getInstance().searchReportedIssues(
                (titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())),
                tagsButton.getTags(),
                formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem()
        );

        if (Objects.equals(orderComboBox.getSelectedItem(), "Crescente"))
            IssueController.getInstance().reOrderIssues();

        if(!success)
            return;

        // Visualizza i risultati usando il pannello specifico per sviluppatori
        new ReportedIssueSearchResultsPanelDeveloper(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}