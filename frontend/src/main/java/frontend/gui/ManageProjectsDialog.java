package frontend.gui;

import frontend.controller.ProjectController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageProjectsDialog extends JDialog {

    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Inserire nome progetto";
    private final JFrame mainFrame;

    public ManageProjectsDialog(JFrame owner) {
        super(owner, "Gestione Progetti", true);
        this.mainFrame = owner;

        // Configurazione pannello principale
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // 1. Header
        setupHeader(mainPanel);

        // 2. Centro: Barra di Ricerca
        setupSearchSection(mainPanel);

        // 3. Risultati: Tabella
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);

        // Uso corretto della firma statica: (gridx, gridy, gridwidth, gridheight, fill, ipadx, ipady, anchor, weightx, weighty, insets)
        Constraints.setConstraints(0, 2, 2, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(owner);
    }

    private void setupHeader(JPanel mainPanel) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.png", 30, 30);
        backButton.addActionListener(e -> dispose());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());


        JButton createButton = new JButton("Crea nuovo progetto");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBackground(new Color(0, 120, 215));
        createButton.setForeground(Color.WHITE);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createButton.addActionListener(e -> {
            CreateProjectDialog dialog = new CreateProjectDialog(mainFrame);
            dialog.setVisible(true);
            performSearch(); // Aggiorna la lista progetti dopo la chiusura
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(createButton, Constraints.getGridBagConstraints());
    }

    private void setupSearchSection(JPanel mainPanel) {
        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

        searchTextField = new JTextField(PLACEHOLDER);
        searchTextField.setPreferredSize(new Dimension(250, 30));
        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
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
        String text = searchTextField.getText();
        String query = text.equals(PLACEHOLDER) ? "" : text;

        ProjectController.getInstance().searchProjectsByName(query);

        List<Integer> ids = ProjectController.getInstance().getProjectsIds();
        List<String> names = ProjectController.getInstance().getProjectsNames();

        updateTable(ids, names);
    }

    private void updateTable(List<Integer> ids, List<String> names) {
        resultsPanel.removeAll();

        Object[][] rowData = new Object[ids.size()][3];
        for (int i = 0; i < ids.size(); i++) {
            rowData[i][0] = ids.get(i);
            rowData[i][1] = names.get(i);
            rowData[i][2] = "Gestisci team";
        }

        String[] columnNames = {"ID Progetto", "Nome Progetto", "Azione"};

        DefaultTableModel model = createTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(new Color(0, 120, 215));
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);


        table.getColumnModel().getColumn(2).setCellEditor(new ManageTeamTextCellEditor(mainFrame, table));

        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private static DefaultTableModel createTableModel(Object[][] rowData, String[] columnNames) {
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Rende editabile solo la colonna 2 per permettere il click sul "tasto"
                return column == 2;
            }
        };
    }
}