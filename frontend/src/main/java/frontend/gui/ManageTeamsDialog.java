package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ManageTeamsDialog extends JDialog {

    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca nome team...";
    private final JFrame mainFrame;

    public ManageTeamsDialog(JFrame owner) {

        super(owner, "Gestione Team", true);
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
        this.setMinimumSize(new Dimension(850, 500));
        this.setLocationRelativeTo(owner);
    }

    private void setupHeader(JPanel mainPanel) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.png", 30, 30);
        backButton.addActionListener(e -> dispose());

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
            performSearch();
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
        String teamName = text.equals(PLACEHOLDER) ? "" : text;

        TeamController.getInstance().searchTeamsByNameAndProject(teamName);

        ArrayList<Integer> ids = (ArrayList<Integer>) TeamController.getInstance().getTeamsIds();
        ArrayList<String> names = (ArrayList<String>) TeamController.getInstance().getTeamsNames();

        updateTable(ids, names);

    }

    private void updateTable(List<Integer> ids, List<String> names) {
        resultsPanel.removeAll();

        Object[][] rowData = new Object[ids.size()][4];
        for (int i = 0; i < ids.size(); i++) {
            rowData[i][0] = ids.get(i);
            rowData[i][1] = names.get(i);
            rowData[i][2] = "Gestisci membri";
            rowData[i][3] = "Visualizza Report mensile";
        }

        String[] columnNames = {"ID Team", "Nome Team", "Membri", "Report"};

        DefaultTableModel model = createTableModel(rowData, columnNames);
        JTable table = new JTable(model);

        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setForeground(new Color(0, 120, 215));
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.getColumnModel().getColumn(2).setCellRenderer(actionRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(actionRenderer);


        table.getColumnModel().getColumn(2).setCellEditor(new ManageMembersTextCellEditor(mainFrame, table));


        table.getColumnModel().getColumn(3).setCellEditor(new ViewReportCellEditor(mainFrame, table));

        table.setRowHeight(35);
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

                return column == 2 || column == 3;
            }
        };
    }
}