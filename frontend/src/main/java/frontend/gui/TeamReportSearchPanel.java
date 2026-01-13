package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Pannello di controllo per la generazione dei Report Mensili dei Team.
 * <p>
 * Questa classe estende {@link RoundedPanel} e fornisce l'interfaccia di input per definire
 * i criteri temporali del report.
 * Contiene:
 * </p>
 * <ul>
 * <li>Un pulsante "Indietro" per tornare alla gestione team.</li>
 * <li>Un menu a tendina (ComboBox) per la selezione del <b>Mese</b>.</li>
 * <li>Una casella di testo per l'inserimento dell'<b>Anno</b>.</li>
 * <li>Un pulsante di ricerca per avviare la generazione del report tramite il Controller.</li>
 * </ul>
 */
public class TeamReportSearchPanel extends RoundedPanel{

    /**
     * Riferimento alla pagina genitore.
     * Necessario per invocare la navigazione (Indietro) e per passare il riferimento
     * alla classe che gestirà i risultati ({@link ReportResults}).
     */
    TeamReportPage searchPage;

    /**
     * Pulsante per la ricerca.
     */
    private IconButton searchButton;

    /**
     * Combobox per il mese.
     */
    private JComboBox<String> monthComboBox;

    /**
     * textField per l'anno.
     */
    private JTextField yearTextField;


    /**
     * Costante per il testo segnaposto dell'anno
     */
    private static final String YEAR_TEXTFIELD_PLACEHOLDER = "Inserisci anno";

    /**
     * Costruttore del pannello di ricerca report.
     * <p>
     * Imposta il layout (GridBagLayout), lo stile grafico e assembla i componenti di input.
     * </p>
     *
     * @param searchPage La pagina principale che ospita questo pannello.
     */
    public TeamReportSearchPanel(TeamReportPage searchPage) {

        super(new GridBagLayout());

        this.searchPage = searchPage;

        setPanel();
        setBackButton();
        setSearchButton();
        setMonthComboBox();
        setYearTextField();
    }

    /**
     * Configura l'aspetto visivo del pannello (sfondo bianco e bordo).
     */
    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }

    /**
     * Configura il pulsante "Indietro".
     * <p>
     * Al click, invoca {@link TeamReportPage#returnToManageTeamsPanel()} per tornare alla lista dei team.
     * </p>
     */
    private void setBackButton() {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> searchPage.returnToManageTeamsPanel());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.FIRST_LINE_START, 0.1f, 0.1f,
                new Insets(10, 10, 0, 0));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pulsante di ricerca centrale.
     * <p>
     * Al click, avvia la logica di validazione e interrogazione definita in {@link #search()}.
     * </p>
     */
    private void setSearchButton() {

        searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 64, 64);

        searchButton.addActionListener(e -> search());

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(searchButton, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di ricerca e generazione del report.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * <ol>
     * <li><b>Validazione Input:</b> Controlla che il campo anno non sia il placeholder e contenga
     * solo cifre numeriche (regex {@code \\d+}).</li>
     * <li><b>Chiamata al Controller:</b> Invoca {@link TeamController#createReport} passando mese e anno selezionati.</li>
     * <li><b>Gestione Successo:</b> Se il controller restituisce true, istanzia {@link ReportResults} che si occuperà
     * di visualizzare i dati nella pagina genitore.</li>
     * <li><b>Gestione Errore:</b> Se l'input non è valido, mostra un {@link FloatingMessage} di errore.</li>
     * </ol>
     * </p>
     */
    private void search() {

        // Controllo validità input (deve essere diverso dal placeholder e contenere solo numeri)
        if(!yearTextField.getText().equals(YEAR_TEXTFIELD_PLACEHOLDER) &&
                yearTextField.getText().matches("\\d+")) {

            // Tenta di generare il report
            boolean success = TeamController.getInstance().createReport(Objects.requireNonNull(monthComboBox.getSelectedItem()).toString(), yearTextField.getText());

            if(!success)
                return;

            // Se successo, visualizza i risultati
            new ReportResults(searchPage);
        } else
            // Errore di validazione input
            new FloatingMessage("L'anno inserito non è valido", searchButton, FloatingMessage.ERROR_MESSAGE);
    }

    /**
     * Configura il menu a tendina per la selezione del mese.
     * <p>
     * Popola la ComboBox con i nomi dei 12 mesi in italiano e la inserisce in un contenitore arrotondato.
     * </p>
     */
    private void setMonthComboBox() {

        monthComboBox = new JComboBox<>();

        monthComboBox.setBackground(ColorsList.EMPTY_COLOR);
        monthComboBox.setBorder(BorderFactory.createEmptyBorder());

        monthComboBox.addItem("Gennaio");
        monthComboBox.addItem("Febbraio");
        monthComboBox.addItem("Marzo");
        monthComboBox.addItem("Aprile");
        monthComboBox.addItem("Maggio");
        monthComboBox.addItem("Giugno");
        monthComboBox.addItem("Luglio");
        monthComboBox.addItem("Agosto");
        monthComboBox.addItem("Settembre");
        monthComboBox.addItem("Ottobre");
        monthComboBox.addItem("Novembre");
        monthComboBox.addItem("Dicembre");

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(monthComboBox);

        Constraints.setConstraints(1, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il campo di testo per l'inserimento dell'anno.
     * <p>
     * Applica il comportamento del placeholder tramite {@link TextComponentFocusBehaviour}
     * e lo inserisce in un contenitore arrotondato.
     * </p>
     */
    private void setYearTextField() {

        yearTextField = new JTextField(YEAR_TEXTFIELD_PLACEHOLDER);

        yearTextField.setBackground(ColorsList.EMPTY_COLOR);
        yearTextField.setBorder(BorderFactory.createEmptyBorder());

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(yearTextField, YEAR_TEXTFIELD_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(yearTextField);

        Constraints.setConstraints(2, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}