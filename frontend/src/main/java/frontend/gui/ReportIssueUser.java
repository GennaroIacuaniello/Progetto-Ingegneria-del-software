package frontend.gui;

import frontend.controller.IssueController;
import frontend.dto.IssueDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Pannello grafico per la creazione di una nuova segnalazione (Issue) da parte di un Utente Standard.
 * <p>
 * Questa classe estende {@link RoundedPanel} e fornisce un form per inserire i dettagli di un problema o richiesta.
 * I campi disponibili per l'utente base sono:
 * </p>
 * <ul>
 * <li><b>Titolo:</b> Breve riassunto della segnalazione.</li>
 * <li><b>Descrizione:</b> Dettagli estesi del problema.</li>
 * <li><b>Tipo:</b> Categoria (Bug, Documentation, Feature, Question).</li>
 * <li><b>Tag:</b> Etichette per la classificazione (tramite {@link TagsButton}).</li>
 * <li><b>Allegati:</b> Caricamento file (tramite {@link FileChooserPanel}).</li>
 * </ul>
 * <br>
 * <p>
 * Nota: Questa classe funge da base per {@link ReportIssueDeveloper}. I metodi di posizionamento (Constraints)
 * sono {@code protected} proprio per permettere alla sottoclasse di riorganizzare il layout quando aggiunge nuovi campi (come la Priorità).
 * </p>
 */
public class ReportIssueUser extends RoundedPanel{

    // Componenti grafici del form
    protected JTextField titleTextField;
    protected JTextArea descriptionTextArea;
    protected JComboBox<String> typeComboBox;
    protected TagsButton tagsButton;
    protected FileChooserPanel fileChooserPanel;
    protected JButton reportButton;

    // Costanti per i testi di default
    protected static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    protected static final String DESCRIPTION_PLACEHOLDER = "Inserisci descrizione";
    protected static final String[] options = {"Bug", "Documentazione", "Feature", "Domanda"};

    /**
     * Costruttore del pannello di segnalazione Utente.
     * <p>
     * Inizializza il layout, configura l'aspetto grafico trasparente, aggiunge i componenti
     * e imposta il titolo nella barra superiore dell'applicazione ("SEGNALA ISSUE").
     * </p>
     *
     * @param mainFrame     Il frame principale (necessario per dialoghi modali come il FileChooser o TagSelector).
     * @param homePanelUser Il pannello Home (necessario per la navigazione "Indietro").
     */
    public ReportIssueUser(JFrame mainFrame, HomePanelUser homePanelUser) {

        super(new GridBagLayout());

        setRoundedPanel();
        setComponents(mainFrame, homePanelUser);

        TitlePanel.getInstance().setTitle("SEGNALA ISSUE");

        setVisible(true);

        revalidate();
        repaint();
    }

    /**
     * Metodo template per l'aggiunta dei componenti al pannello.
     * <p>
     * Richiama in sequenza i metodi per costruire le varie parti del form.
     * È marcato {@code protected} per permettere alle sottoclassi di sovrascriverlo
     * se l'ordine o la composizione dei componenti deve cambiare.
     * </p>
     */
    protected void setComponents(JFrame mainFrame, HomePanelUser  homePanelUser) {

        setBackButton(homePanelUser);
        setTitleTextField();
        setDescriptionTextArea();
        setTypeComboBox();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setReportButton(homePanelUser);
    }

    /**
     * Configura lo sfondo del pannello per essere trasparente.
     * Permette di vedere lo sfondo dell'applicazione sottostante.
     */
    private void setRoundedPanel() {

        setRoundBorderColor(ColorsList.EMPTY_COLOR);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    /**
     * Aggiunge il pulsante "Indietro".
     *
     * @param homePanelUser Il riferimento per tornare alla schermata precedente.
     */
    protected void setBackButton(HomePanelUser homePanelUser) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> homePanelUser.returnToDefaultContentPanel());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f,
                new Insets(5, 5, 5, 5));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il campo di testo per il Titolo.
     * <p>
     * Include la gestione del placeholder e l'avvolgimento in un {@link RoundedPanel} per lo stile.
     * </p>
     */
    protected void setTitleTextField() {

        titleTextField = new JTextField(TITLE_PLACEHOLDER, 30);
        titleTextField.setBorder(BorderFactory.createEmptyBorder());
        titleTextField.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleTextField.setHorizontalAlignment(SwingConstants.CENTER);
        titleTextField.setPreferredSize(new Dimension(300, 30));
        titleTextField.setMinimumSize(new Dimension(300, 30));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(titleTextField);

        Constraints.setConstraints(0, 1, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 160, 10, 160));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura l'area di testo per la Descrizione.
     * <p>
     * Inserita in uno {@link JScrollPane} per gestire testi lunghi.
     * </p>
     */
    protected void setDescriptionTextArea() {

        descriptionTextArea = new JTextArea(DESCRIPTION_PLACEHOLDER, 10, 60);
        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionTextArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(descriptionTextArea, DESCRIPTION_PLACEHOLDER);

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);
        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(ColorsList.EMPTY_COLOR);
        tmpScrollPane.setViewportView(descriptionTextArea);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 80, 10, 80));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il menu a tendina per il Tipo di issue.
     */
    protected void setTypeComboBox() {

        typeComboBox = new JComboBox<>(options);
        typeComboBox.setBorder(BorderFactory.createEmptyBorder());
        typeComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeComboBox);

        Constraints.setConstraints(0, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Tipo: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza il componente per la gestione dei Tag.
     */
    protected void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        setTagsButtonConstraints(); // Usa metodo separato per override

        this.add(tagsButton, Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza il componente per il caricamento dei file.
     */
    protected void setFileChooserPanel() {

        fileChooserPanel = new FileChooserPanel();

        setFileChooserPanelConstraints(); // Usa metodo separato per override

        this.add(fileChooserPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pulsante di invio "Report".
     * <p>
     * Gestisce la validazione minima (il titolo non deve essere vuoto) e scatena l'evento di report.
     * </p>
     */
    protected void setReportButton(HomePanelUser homePanelUser) {

        reportButton = new JButton("Report");

        reportButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        reportButton.setBorder(BorderFactory.createEmptyBorder());
        reportButton.setBackground(ColorsList.EMPTY_COLOR);

        reportButton.addActionListener(e -> {
            // Validazione base
            if (titleTextField.getText().equals(TITLE_PLACEHOLDER))
                new FloatingMessage("Inserire il titolo per segnalare una issue", reportButton, FloatingMessage.ERROR_MESSAGE);
            else
                report(homePanelUser);
        });

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(reportButton);

        tmpPanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
        tmpPanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(0, 5, 4, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di invio della segnalazione.
     * <p>
     * 1. Raccoglie i dati dai campi di input e popola un {@link IssueDTO}.<br>
     * 2. <b>Nota Importante:</b> Per l'utente standard, la priorità viene impostata automaticamente a "Media"
     * poiché non ha i permessi per deciderla.<br>
     * 3. Invoca {@link IssueController#reportIssue} passando DTO, tag e file.<br>
     * 4. Se l'operazione ha successo, torna alla schermata principale.
     * </p>
     */
    protected void report(HomePanelUser homePanelUser) {

        IssueDTO issue = new IssueDTO();

        issue.setTitle(titleTextField.getText());
        issue.setDescription((descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER) ? "" : descriptionTextArea.getText()));
        issue.setTypeWithString(formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())));

        // Default Priority per User: MEDIA
        issue.setPriority(IssueController.getInstance().priorityStringToInt("Media"));

        boolean success = IssueController.getInstance().reportIssue(issue, tagsButton.getTags(), fileChooserPanel.getSelectedFile());

        if(!success)
            return;

        homePanelUser.returnToDefaultContentPanel();
    }

    /**
     * Helper per creare etichette trasparenti.
     */
    protected JLabel createTransparentLabel(String text) {

        JLabel label = new JLabel(text);

        label.setBorder(BorderFactory.createEmptyBorder());
        label.setBackground(ColorsList.EMPTY_COLOR);

        return label;
    }

    /**
     * Definisce i vincoli di layout per il pulsante dei Tag.
     * <p>
     * È un metodo separato e {@code protected} per consentire a {@link ReportIssueDeveloper}
     * di spostare questo pulsante (cambiando gridx/gridy) per fare spazio al campo Priorità.
     * </p>
     */
    protected void setTagsButtonConstraints() {

        Constraints.setConstraints(0, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }

    /**
     * Definisce i vincoli di layout per il pannello File Chooser.
     * <p>
     * È un metodo separato e {@code protected} per consentire l'override da parte delle sottoclassi
     * per riorganizzare la griglia.
     * </p>
     */
    protected void setFileChooserPanelConstraints() {

        Constraints.setConstraints(1, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }

    /**
     * Metodo helper per convertire l'etichetta del tipo (UI) nel valore enum del backend.
     * Es. "Domanda" -> "QUESTION".
     */
    protected String formatIssueType(Object issueType) {

        return switch (issueType.toString()) {
            case "Bug" -> "Bug";
            case "Documentazione" -> "Documentation";
            case "Feature" -> "Feature";
            case "Domanda" -> "Question";
            default -> null;
        };
    }
}