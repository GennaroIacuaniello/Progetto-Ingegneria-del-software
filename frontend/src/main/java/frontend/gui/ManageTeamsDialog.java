package frontend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Dialog per la gestione dei team all'interno di un progetto.
 * Permette di cercare team e accedere alla gestione dei membri.
 */
public class ManageTeamsDialog extends JDialog {

    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca nome team...";
    private final JFrame mainFrame;
    private final int projectId;

    public ManageTeamsDialog(JFrame owner, int projectId) {
        super(owner, "Gestione Team", true);
        this.mainFrame = owner;
        this.projectId = projectId;

        // Pannello principale con GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // 1. Header: Indietro e Nuovo Team
        setupHeader(mainPanel);

        // 2. Search Bar
        setupSearchSection(mainPanel);

        // 3. Area Risultati
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);

        // Vincoli per l'area risultati (weightx/y = 1.0 per espandersi)
        Constraints.setConstraints(0, 2, 2, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setMinimumSize(new Dimension(650, 500));
        this.setLocationRelativeTo(owner);
    }

    private void setupHeader(JPanel mainPanel) {
        // Pulsante Indietro (Sinistra)
        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.png", 30, 30);
        backButton.addActionListener(e -> dispose());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());

        // Pulsante Crea Nuovo Team (Destra) - Stile AddNewFlightDialog
        JButton createTeamButton = new JButton("Crea nuovo team");
        createTeamButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createTeamButton.setBackground(new Color(0, 120, 215));
        createTeamButton.setForeground(Color.WHITE);
        createTeamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createTeamButton.addActionListener(e -> {
            // Passiamo il projectId che la classe ManageTeamsDialog già possiede
            CreateTeamDialog dialog = new CreateTeamDialog(mainFrame, this.projectId);
            dialog.setVisible(true);
            performSearch(); // Aggiorna la lista dei team per mostrare quello nuovo
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(createTeamButton, Constraints.getGridBagConstraints());
    }

    private void setupSearchSection(JPanel mainPanel) {
        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

        searchTextField = new JTextField(PLACEHOLDER);
        searchTextField.setPreferredSize(new Dimension(250, 30));
        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // Comportamento placeholder
        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, PLACEHOLDER);

        IconButton searchButton = new IconButton("/frontend/gui/images/searchButton.png", 25, 25);
        searchButton.addActionListener(e -> performSearch());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchButton, Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchTextField, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 1, 2, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(10, 0, 10, 0));
        mainPanel.add(searchWrapper, Constraints.getGridBagConstraints());
    }

    private void performSearch() {
        // Qui andrebbe la chiamata al controller dei Team
        // Per ora simuliamo dei dati
        updateTable();
    }

    private void updateTable() {
        resultsPanel.removeAll();

        String[] columnNames = {"ID Team", "Nome Team", "Azione"};

        // Esempio dati simulati
        Object[][] data = {
                {1, "Sviluppatori Backend", "Gestisci membri"},
                {2, "Team UI/UX", "Gestisci membri"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Editabile solo la colonna del "tasto"
            }
        };

        JTable table = new JTable(model);

        // Renderer per la colonna Azione (testo blu centrato)
        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setForeground(new Color(0, 120, 215));
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getColumnModel().getColumn(2).setCellRenderer(actionRenderer);

        table.getColumnModel().getColumn(2).setCellEditor(new ManageMembersTextCellEditor(mainFrame, table));

        table.setRowHeight(35);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}