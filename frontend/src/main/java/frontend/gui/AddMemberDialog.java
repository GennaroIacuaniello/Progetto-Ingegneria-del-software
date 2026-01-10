package frontend.gui;

import frontend.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class AddMemberDialog extends JDialog {

    private JTextField searchTextField;
    private final JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca email utente...";
    private final JFrame mainFrame;

    public AddMemberDialog(JFrame owner) {
        super(owner, "Aggiungi Nuovo Membro", true);
        this.mainFrame = owner;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        setupSearchSection(mainPanel);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setSize(600, 450);
        this.setLocationRelativeTo(owner);

        performSearch();
    }

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


        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 0, GridBagConstraints.CENTER, new Insets(0, 0, 10, 0));
        mainPanel.add(searchContainer, Constraints.getGridBagConstraints());
    }


    public void performSearch() {

        String text = searchTextField.getText();
        String userEmail = text.equals(PLACEHOLDER) ? "" : text;

        UserController.getInstance().searchDevOrAdminByEmail(userEmail);

        updateResultsTable(UserController.getInstance().getUsersEmails());
    }


    public void updateResultsTable(List<String> emails) {

        resultsPanel.removeAll();

        String[] cols = {"Email", "Azione"};
        Object[][] data = new Object[emails.size()][2];

        for (int i = 0; i < emails.size(); i++) {
            data[i][0] = emails.get(i);
            data[i][1] = "Aggiungi";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1; }
        };

        JTable table = new JTable(model);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setForeground(new Color(0, 120, 215));
        renderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        table.getColumnModel().getColumn(1).setCellEditor(
                new AddMemberActionCellEditor(mainFrame, table, this)
                //
                 //
                 //
                 //
        );

        table.setRowHeight(35);
        resultsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}