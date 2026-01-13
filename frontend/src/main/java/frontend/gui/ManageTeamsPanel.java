package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pannello principale per la gestione dei Team all'interno di un Progetto.
 * <p>
 * Questa classe estende {@link RoundedPanel} e fornisce un'interfaccia utente completa per:
 * <ul>
 * <li>Visualizzare l'elenco dei team associati al progetto corrente.</li>
 * <li>Cercare team specifici tramite nome.</li>
 * <li>Creare nuovi team (tramite {@link CreateTeamDialog}).</li>
 * <li>Accedere alla gestione dei membri del team (tramite {@link ManageMembersTextCellEditor}).</li>
 * <li>Visualizzare i report mensili del team (tramite {@link ViewReportCellEditor}).</li>
 * </ul>
 * </p>
 */
public class ManageTeamsPanel extends RoundedPanel {

    private JTextField searchTextField;
    private final JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca nome team...";
    private final JFrame mainFrame;
    private final HomePanelUser homePanel;
    private final String TITLE_PLACEHOLDER = "GESTISCI TEAMS";

    /**
     * Costruttore del pannello di gestione team.
     * <p>
     * Inizializza il layout, configura l'aspetto grafico (sfondo bianco, bordi),
     * imposta l'header (con pulsanti di navigazione e creazione), la barra di ricerca
     * e prepara l'area per i risultati.
     * Infine, esegue una ricerca iniziale per popolare la tabella con tutti i team esistenti.
     * </p>
     *
     * @param owner     Il frame principale dell'applicazione.
     * @param homePanel Il pannello genitore (HomePanelUser) necessario per la navigazione (tornare indietro).
     */
    public ManageTeamsPanel(JFrame owner, HomePanelUser homePanel) {

        super(new GridBagLayout());
        this.mainFrame = owner;
        this.homePanel = homePanel;

        this.setBackground(Color.WHITE);
        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        setupHeader(this);
        setupSearchSection(this);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);

        Constraints.setConstraints(0, 2, 2, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, // weighty = 1.0f attira lo spazio verticale rimanente
                new Insets(20, 0, 0, 0));
        this.add(resultsPanel, Constraints.getGridBagConstraints());

        // Imposta il titolo nella barra superiore dell'applicazione
        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);

        performSearch();
    }

    /**
     * Configura la parte superiore del pannello.
     * <p>
     * Aggiunge:
     * <ul>
     * <li>Un pulsante "Indietro" per tornare alla dashboard principale del progetto.</li>
     * <li>Un pulsante "Crea nuovo team" che apre la finestra di dialogo {@link CreateTeamDialog}.</li>
     * </ul>
     * </p>
     *
     * @param mainPanel Il pannello a cui aggiungere i componenti.
     */
    private void setupHeader(JPanel mainPanel) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 30, 30);
        backButton.addActionListener(e -> homePanel.returnToDefaultContentPanel());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());

        JButton createTeamButton = new JButton("Crea nuovo team");
        createTeamButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createTeamButton.setBackground(new Color(0, 120, 215));
        createTeamButton.setForeground(Color.WHITE);
        createTeamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createTeamButton.addActionListener(e -> {
            CreateTeamDialog dialog = new CreateTeamDialog(mainFrame);
            dialog.setVisible(true);
            // Aggiorna la tabella dopo la creazione di un nuovo team
            performSearch();
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(createTeamButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura la sezione di ricerca.
     * <p>
     * Crea una barra di ricerca stilizzata contenente un campo di testo e un pulsante icona.
     * La ricerca viene avviata sia premendo Invio nel campo di testo, sia cliccando sulla lente.
     * </p>
     *
     * @param mainPanel Il pannello a cui aggiungere la sezione di ricerca.
     */
    private void setupSearchSection(JPanel mainPanel) {

        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

        Dimension wrapperDim = new Dimension(350, 50);
        searchWrapper.setPreferredSize(wrapperDim);
        searchWrapper.setMinimumSize(wrapperDim);
        searchWrapper.setMaximumSize(wrapperDim);

        searchWrapper.setBorder(new EmptyBorder(5, 5, 5, 5));

        searchTextField = new JTextField(PLACEHOLDER);

        Dimension textDim = new Dimension(250, 30);
        searchTextField.setPreferredSize(textDim);
        searchTextField.setMinimumSize(textDim);
        searchTextField.setMaximumSize(textDim);

        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, PLACEHOLDER);

        searchTextField.addActionListener(e -> performSearch());

        IconButton searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 25, 25);
        searchButton.addActionListener(e -> performSearch());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 5, 0, 0));
        searchWrapper.add(searchButton, Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(0, 5, 0, 10));
        searchWrapper.add(searchTextField, Constraints.getGridBagConstraints());

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchContainer.setOpaque(false);
        searchContainer.add(searchWrapper);

        Constraints.setConstraints(0, 1, 2, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f,
                new Insets(10, 0, 10, 0));
        mainPanel.add(searchContainer, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di ricerca interrogando il controller.
     * <p>
     * 1. Legge il testo dal campo di ricerca (gestendo il placeholder).<br>
     * 2. Chiama {@link TeamController#searchTeamsByNameAndProject(String)}.<br>
     * 3. Se la ricerca ha successo, recupera gli ID e i nomi dei team e aggiorna la tabella.
     * </p>
     */
    private void performSearch() {

        String text = searchTextField.getText();
        String teamName = text.equals(PLACEHOLDER) ? "" : text;

        boolean success = TeamController.getInstance().searchTeamsByNameAndProject(teamName);

        if(!success)
            return;

        ArrayList<Integer> ids = (ArrayList<Integer>) TeamController.getInstance().getTeamsIds();
        ArrayList<String> names = (ArrayList<String>) TeamController.getInstance().getTeamsNames();

        updateTable(ids, names);

    }

    /**
     * Aggiorna la tabella dei risultati.
     * <p>
     * Costruisce una {@link JTable} con 4 colonne:
     * <ol>
     * <li><b>ID Team:</b> Identificativo numerico.</li>
     * <li><b>Nome Team:</b> Nome del team.</li>
     * <li><b>Membri:</b> Colonna interattiva ("Gestisci membri") che usa {@link ManageMembersTextCellEditor}.</li>
     * <li><b>Report:</b> Colonna interattiva ("Visualizza Report mensile") che usa {@link ViewReportCellEditor}.</li>
     * </ol>
     * </p>
     *
     * @param ids   Lista degli ID dei team trovati.
     * @param names Lista dei nomi dei team trovati.
     */
    private void updateTable(List<Integer> ids, List<String> names) {
        resultsPanel.removeAll();

        Object[][] rowData = new Object[ids.size()][4];
        for (int i = 0; i < ids.size(); i++) {
            rowData[i][0] = ids.get(i);
            rowData[i][1] = names.get(i);
            rowData[i][2] = "Gestisci membri";       // Testo cliccabile
            rowData[i][3] = "Visualizza Report mensile"; // Testo cliccabile
        }

        String[] columnNames = {"ID Team", "Nome Team", "Membri", "Report"};

        DefaultTableModel model = createTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        // Configurazione stile per le colonne azione (testo blu centrato)
        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setForeground(new Color(0, 120, 215));
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.getColumnModel().getColumn(2).setCellRenderer(actionRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);

        // Impostazione degli editor per gestire i click sulle azioni
        table.getColumnModel().getColumn(2).setCellEditor(new ManageMembersTextCellEditor(mainFrame, table));
        table.getColumnModel().getColumn(3).setCellEditor(new ViewReportCellEditor(table, homePanel, this));

        table.setRowHeight(35);

        JTableHeader header = table.getTableHeader();

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(ColorsList.TABLE_HEADER_BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(table);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    /**
     * Helper statico per creare il TableModel.
     * <p>
     * Rende editabili (cliccabili) solo le colonne 2 (Gestisci membri) e 3 (Visualizza Report).
     * </p>
     */
    private static DefaultTableModel createTableModel(Object[][] rowData, String[] columnNames) {
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 2 || column == 3;
            }
        };
    }

    /**
     * Metodo di utility per ripristinare questo pannello come contenuto principale.
     * <p>
     * Viene chiamato quando si torna indietro da una vista secondaria (come la visualizzazione del report).
     * Reimposta questo pannello in {@link HomePanelUser} e ripristina il titolo.
     * </p>
     */
    public void returnToManageTeamsPanel() {

        homePanel.setContentPanel(this);
        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);
    }
}