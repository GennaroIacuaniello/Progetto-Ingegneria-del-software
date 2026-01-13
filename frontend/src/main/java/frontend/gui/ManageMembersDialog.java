package frontend.gui;

import frontend.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Finestra di dialogo per la gestione dei membri di un team.
 * <p>
 * Questa classe estende {@link JDialog} e fornisce un'interfaccia completa per:
 * <ul>
 * <li>Visualizzare l'elenco dei membri attualmente assegnati al team.</li>
 * <li>Cercare membri specifici tramite barra di ricerca (filtro per email).</li>
 * <li>Aggiungere nuovi membri (aprendo {@link AddMemberDialog}).</li>
 * <li>Rimuovere membri esistenti tramite un'azione dedicata nella tabella.</li>
 * </ul>
 * I dati vengono recuperati e gestiti tramite {@link UserController}.
 * </p>
 */
public class ManageMembersDialog extends JDialog {

    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca email membro...";
    private final JFrame mainFrame;

    /**
     * Costruttore della finestra di gestione membri.
     * <p>
     * Inizializza il layout principale, configura l'header (pulsanti indietro e aggiungi),
     * la sezione di ricerca e il pannello dei risultati.
     * Al termine dell'inizializzazione, esegue automaticamente una ricerca vuota per mostrare
     * tutti i membri correnti.
     * </p>
     *
     * @param owner Il frame principale che possiede questa finestra modale.
     */
    public ManageMembersDialog(JFrame owner) {
        super(owner, "Gestione Membri Team", true);
        this.mainFrame = owner;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        setupHeader(mainPanel);
        setupSearchSection(mainPanel);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);

        Constraints.setConstraints(0, 2, 2, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setMinimumSize(new Dimension(700, 500));
        this.setLocationRelativeTo(owner);

        performSearch();
    }

    /**
     * Configura la parte superiore della finestra.
     * <p>
     * Aggiunge:
     * <ul>
     * <li>Un pulsante "Indietro" per chiudere la finestra.</li>
     * <li>Un pulsante "Aggiungi membro" che apre la dialog {@link AddMemberDialog}.</li>
     * </ul>
     * </p>
     *
     * @param mainPanel Il pannello principale a cui aggiungere i componenti.
     */
    private void setupHeader(JPanel mainPanel) {
        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 30, 30);
        backButton.addActionListener(e -> dispose());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());

        JButton addMemberButton = new JButton("Aggiungi membro");
        addMemberButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addMemberButton.setBackground(new Color(0, 120, 215));
        addMemberButton.setForeground(Color.WHITE);
        addMemberButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMemberButton.addActionListener(e -> {
            AddMemberDialog dialog = new AddMemberDialog(mainFrame);
            dialog.setVisible(true);
            // Aggiorna la lista dopo la chiusura del dialog di aggiunta
            performSearch();
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(addMemberButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura la barra di ricerca stilizzata.
     * <p>
     * Include un campo di testo con placeholder e un pulsante lente d'ingrandimento.
     * La ricerca viene attivata sia premendo Invio nel campo di testo, sia cliccando sul pulsante.
     * </p>
     *
     * @param mainPanel Il pannello principale.
     */
    private void setupSearchSection(JPanel mainPanel) {

        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());

        Dimension wrapperDim = new Dimension(350, 50);
        searchWrapper.setPreferredSize(wrapperDim);
        searchWrapper.setMinimumSize(wrapperDim);
        searchWrapper.setMaximumSize(wrapperDim);

        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

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

        Constraints.setConstraints(0, 1, 2, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(10, 0, 10, 0));
        mainPanel.add(searchWrapper, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la ricerca dei membri tramite il controller.
     * <p>
     * 1. Recupera il testo dalla barra di ricerca (se è il placeholder, usa una stringa vuota).<br>
     * 2. Chiama {@link UserController#searchDevOrAdminByEmailAndTeam(String)}.<br>
     * 3. Se la chiamata ha successo, recupera la lista di email e aggiorna la tabella.
     * </p>
     */
    public void performSearch() {
        String text = searchTextField.getText();
        String userMail = text.equals(PLACEHOLDER) ? "" : text;

        boolean success = UserController.getInstance().searchDevOrAdminByEmailAndTeam(userMail);

        if(!success)
            return;

        ArrayList<String> emails = (ArrayList<String>) UserController.getInstance().getUsersEmails();

        updateTable(emails);
    }

    /**
     * Aggiorna la tabella dei risultati con la lista di email fornita.
     * <p>
     * Ricostruisce il pannello dei risultati creando una nuova {@link JTable}.
     * Configura due colonne: "Email" (solo lettura) e "Azione" (cliccabile per rimuovere).
     * Utilizza {@link RemoveMemberCellEditor} per gestire la logica di cancellazione.
     * </p>
     *
     * @param emails Lista di email dei membri da visualizzare.
     */
    public void updateTable(List<String> emails) {
        resultsPanel.removeAll();

        Object[][] rowData = new Object[emails.size()][2];
        for (int i = 0; i < emails.size(); i++) {
            rowData[i][0] = emails.get(i);
            rowData[i][1] = "Rimuovi membro"; // Testo cliccabile
        }

        String[] columnNames = {"Email", "Azione"};

        DefaultTableModel model = createTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        // Configurazione renderer per la colonna Azione (testo rosso, centrato)
        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setForeground(new Color(220, 53, 69)); // Rosso per azione distruttiva
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getColumnModel().getColumn(1).setCellRenderer(actionRenderer);

        // Configurazione editor per gestire il click sulla rimozione
        table.getColumnModel().getColumn(1).setCellEditor(new RemoveMemberCellEditor(mainFrame, table, this));

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
     * Helper per creare un TableModel personalizzato.
     * <p>
     * Definisce che solo la colonna 1 ("Azione") è editabile, permettendo il click sul pulsante "Rimuovi".
     * La colonna "Email" rimane in sola lettura.
     * </p>
     */
    private static DefaultTableModel createTableModel(Object[][] rowData, String[] columnNames) {
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
    }
}