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


public class ManageTeamsPanel extends RoundedPanel {

    private JTextField searchTextField;
    private final JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca nome team...";
    private final JFrame mainFrame;
    private final HomePanelUser homePanel;
    private final String TITLE_PLACEHOLDER = "GESTISCI TEAMS";

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
                0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, // weighty = 1.0f attira lo spazio
                new Insets(20, 0, 0, 0));
        this.add(resultsPanel, Constraints.getGridBagConstraints());

        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);

        performSearch();
    }

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

    private static DefaultTableModel createTableModel(Object[][] rowData, String[] columnNames) {
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 2 || column == 3;
            }
        };
    }

    public void returnToManageTeamsPanel() {

        homePanel.setContentPanel(this);
        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);
    }
}