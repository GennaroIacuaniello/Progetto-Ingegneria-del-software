package frontend.gui;

import frontend.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManageMembersDialog extends JDialog {

    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca email membro...";
    private final JFrame mainFrame;

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
            performSearch();
        });

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, 0.01f, 0.01f, new Insets(0, 0, 10, 0));
        mainPanel.add(addMemberButton, Constraints.getGridBagConstraints());
    }

    private void setupSearchSection(JPanel mainPanel) {
        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

        searchTextField = new JTextField(PLACEHOLDER);
        searchTextField.setPreferredSize(new Dimension(250, 30));
        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, PLACEHOLDER);

        IconButton searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 25, 25);
        searchButton.addActionListener(e -> performSearch());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchButton, Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchTextField, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 1, 2, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(10, 0, 10, 0));
        mainPanel.add(searchWrapper, Constraints.getGridBagConstraints());
    }


    public void performSearch() {

        String text = searchTextField.getText();
        String userMail = text.equals(PLACEHOLDER) ? "" : text;


        UserController.getInstance().searchDevOrAdminByEmailAndTeam(userMail);

        ArrayList<String> emails = (ArrayList<String>) UserController.getInstance().getUsersEmails();

        updateTable(emails);
    }


    public void updateTable(List<String> emails) {

        resultsPanel.removeAll();

        Object[][] rowData = new Object[emails.size()][3];
        for (int i = 0; i < emails.size(); i++) {
            rowData[i][0] = emails.get(i);
            rowData[i][1] = "Rimuovi membro";
        }

        String[] columnNames = {"Email", "Azione"};

        DefaultTableModel model = createTableModel(rowData, columnNames);

        JTable table = new JTable(model);

        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setForeground(new Color(220, 53, 69));
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getColumnModel().getColumn(1).setCellRenderer(actionRenderer);

        table.getColumnModel().getColumn(1).setCellEditor(new RemoveMemberCellEditor(mainFrame, table, this));

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
                return column == 1;
            }
        };
    }
}