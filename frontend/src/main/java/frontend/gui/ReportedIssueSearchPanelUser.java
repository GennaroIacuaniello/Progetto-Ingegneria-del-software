package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Pannello di ricerca delle segnalazioni (Reported Issues) per l'Utente Standard.
 * <p>
 * Questa classe estende {@link RoundedPanel} e fornisce l'interfaccia grafica per permettere all'utente
 * di cercare e filtrare le issue che ha segnalato.
 * I filtri disponibili in questa versione base sono:
 * </p>
 * <ul>
 * <li><b>Titolo:</b> Ricerca testuale.</li>
 * <li><b>Stato:</b> Filtro sullo stato di avanzamento (To do, Assegnate, Risolte).</li>
 * <li><b>Tag:</b> Filtro per etichette tramite {@link TagsButton}.</li>
 * <li><b>Tipo:</b> Filtro per tipologia (Bug, Feature, Documentazione, Domanda).</li>
 * </ul>
 * <p>
 * Questa classe è progettata per essere estesa da {@link ReportedIssueSearchPanelDeveloper}.
 * </p>
 */
public class ReportedIssueSearchPanelUser extends RoundedPanel{

    /**
     * Pagina per la ricerca.
     */
    protected SearchReportedIssuePageUser searchPage;

    /**
     * Panel per il titolo.
     */
    private RoundedPanel titlePanel;

    /**
     * TextField.
     */
    protected JTextField titleTextField;

    /**
     * ComboBox per lo status.
     */
    protected JComboBox<String> statusComboBox;

    /**
     * Pulsante per i tag.
     */
    protected TagsButton tagsButton;

    /**
     * ComboBox per il tipo.
     */
    protected JComboBox<String> typeComboBox;

    // Costanti per i placeholder e le opzioni dei menu a tendina
    /**
     * Placeholder per il titolo.
     */
    protected static final String TITLE_PLACEHOLDER = "Inserisci titolo";

    /**
     * Placeholder per "Tutte".
     */
    protected static final String ALL_PLACEHOLDER = "Tutte";

    /**
     * Placeholder per le opzioni dello status della issue.
     */
    private static final String[] statusOptions = {ALL_PLACEHOLDER , "To do", "Assegnate", "Risolte"};

    /**
     * Placeholder per le opzioni per il tipo ella issue.
     */
    private static final String[] typeOptions = {ALL_PLACEHOLDER, "Bug", "Documentazione", "Feature", "Domanda"};


    /**
     * Costruttore del pannello di ricerca.
     * <p>
     * Imposta il layout (GridBagLayout), configura lo stile del pannello e inizializza
     * tutti i componenti grafici: pulsante indietro, barra di ricerca titolo e i vari filtri.
     * </p>
     *
     * @param mainFrame  Il frame principale dell'applicazione.
     * @param searchPage La pagina contenitore (utilizzata per la navigazione "Indietro").
     */
    public ReportedIssueSearchPanelUser(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(new GridBagLayout());

        this.searchPage = searchPage;

        setPanel();

        setBackButton(searchPage);
        setUpperPanel();
        setSearchButton(mainFrame);
        setTitleTextField(mainFrame);
        setStatusComboBox();
        setTagsButton(mainFrame);
        setTypeComboBox();
    }

    /**
     * Configura l'aspetto del pannello (sfondo bianco, bordo colorato).
     */
    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }

    /**
     * Aggiunge il pulsante "Indietro".
     * <p>
     * Al click, invoca il metodo {@code homePanelReturnToDefaultContentPane()} della pagina genitore
     * per tornare alla visualizzazione precedente.
     * </p>
     */
    private void setBackButton(SearchReportedIssuePageUser searchPage) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> searchPage.homePanelReturnToDefaultContentPane());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f,
                new Insets(5, 5, 5, 5));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    /**
     * Crea il pannello superiore che conterrà la barra di ricerca testuale e il pulsante lente.
     */
    private void setUpperPanel() {

        titlePanel = new RoundedPanel(new GridBagLayout());

        titlePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        titlePanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 1, 9, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                1f, 0.5f);
        this.add(titlePanel, Constraints.getGridBagConstraints());
    }

    /**
     * Aggiunge il pulsante con l'icona della lente d'ingrandimento per avviare la ricerca.
     */
    private void setSearchButton(JFrame mainFrame) {

        IconButton searchIssuesButton = new IconButton("/frontend/gui/images/searchButton.svg", 32, 32);

        searchIssuesButton.addActionListener(e -> searchButtonActionListener(mainFrame));

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        titlePanel.add(searchIssuesButton, Constraints.getGridBagConstraints());
    }

    /**
     * Logica principale di esecuzione della ricerca.
     * <p>
     * 1. Recupera il testo dal campo titolo (ignorando il placeholder).<br>
     * 2. Raccoglie i valori selezionati dai ComboBox (Stato, Tipo) e li formatta per il backend.<br>
     * 3. Recupera i tag selezionati.<br>
     * 4. Invoca {@link IssueController#searchReportedIssues}.<br>
     * 5. Se la ricerca ha successo, istanzia {@link ReportedIssueSearchResultsPanelUser} per mostrare i risultati.
     * <br>
     * Nota: Questo metodo è {@code protected} per permettere l'override nella classe Developer.
     * </p>
     */
    protected void searchButtonActionListener(JFrame mainFrame) {

        boolean success = IssueController.getInstance().searchReportedIssues(
                (titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())),
                tagsButton.getTags(),
                formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())),
                null // Priorità è null per l'utente base
        );

        if(!success)
            return;

        new ReportedIssueSearchResultsPanelUser(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles(), "Decrescente");
    }

    /**
     * Configura il campo di testo per la ricerca per titolo.
     */
    private void setTitleTextField(JFrame mainFrame) {

        titleTextField = new JTextField(TITLE_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        titleTextField.setPreferredSize(new Dimension(150, 20));
        titleTextField.setMinimumSize(new Dimension(150, 20));
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        // Premendo invio nel campo di testo si avvia la ricerca
        titleTextField.addActionListener(e -> searchButtonActionListener(mainFrame));

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 0, 5, 5));
        titlePanel.add(titleTextField, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il menu a tendina per il filtro Stato.
     */
    private void setStatusComboBox() {

        statusComboBox = new JComboBox<>(statusOptions);

        statusComboBox.setBorder(BorderFactory.createEmptyBorder());
        statusComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(statusComboBox);

        Constraints.setConstraints(0, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Stato: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Aggiunge il pulsante per la gestione dei filtri Tag.
     */
    private void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        Constraints.setConstraints(2, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tagsButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il menu a tendina per il filtro Tipo.
     */
    private void setTypeComboBox() {

        typeComboBox = new JComboBox<>(typeOptions);

        typeComboBox.setBackground(ColorsList.EMPTY_COLOR);
        typeComboBox.setBorder(BorderFactory.createEmptyBorder());

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeComboBox);

        Constraints.setConstraints(3, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Tipo: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(4, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Metodo helper per convertire l'etichetta del tipo (UI) nel valore enum del backend.
     * Es. "Domanda" -> "QUESTION".
     */
    protected String formatIssueType(Object issueType) {

        return switch (issueType.toString()) {
            case "Bug" -> "BUG";
            case "Documentazione" -> "DOCUMENTATION";
            case "Feature" -> "FEATURE";
            case "Domanda" -> "QUESTION";
            default -> null;
        };
    }

    /**
     * Metodo helper per convertire l'etichetta dello stato (UI) nel valore enum del backend.
     * Es. "Risolte" -> "RESOLVED".
     */
    protected String formatIssueStatus(Object issueStatus) {

        return switch (issueStatus.toString()) {
            case "Assegnate" -> "ASSIGNED";
            case "Risolte" -> "RESOLVED";
            case "To do" -> "TODO";
            default -> null;
        };
    }

    /**
     * Metodo helper per creare label trasparenti (usate per le etichette "Stato:", "Tipo:", ecc.).
     */
    protected JLabel createTransparentLabel(String text) {

        JLabel label = new JLabel(text);

        label.setBackground(ColorsList.EMPTY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder());

        return label;
    }
}