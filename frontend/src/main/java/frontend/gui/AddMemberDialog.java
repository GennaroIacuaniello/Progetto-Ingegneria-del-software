package frontend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AddMemberDialog extends JDialog {
    private JTextField searchTextField;
    private JPanel resultsPanel;
    private final String PLACEHOLDER = "Cerca email utente...";
    private final JFrame mainFrame;
    private final int teamId;

    public AddMemberDialog(JFrame owner, int teamId) {
        super(owner, "Aggiungi Nuovo Membro", true);
        this.mainFrame = owner;
        this.teamId = teamId;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Barra di ricerca (Stile SearchProjectPanelUser)
        setupSearchSection(mainPanel);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setSize(500, 400);
        this.setLocationRelativeTo(owner);
    }

    private void setupSearchSection(JPanel mainPanel) {
        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);

        searchTextField = new JTextField(PLACEHOLDER);
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setBorder(BorderFactory.createEmptyBorder());
        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, PLACEHOLDER); //

        IconButton searchButton = new IconButton("/frontend/gui/images/searchButton.png", 25, 25);
        searchButton.addActionListener(e -> updateResultsTable());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchButton, Constraints.getGridBagConstraints());
        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER);
        searchWrapper.add(searchTextField, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 1, 0, GridBagConstraints.CENTER);
        mainPanel.add(searchWrapper, Constraints.getGridBagConstraints());
    }

    public void updateResultsTable() {
        resultsPanel.removeAll();
        String[] cols = {"Email", "Azione"};
        Object[][] data = {{"nuovo.utente@test.it", "Aggiungi"}}; // Dati simulati

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1; }
        };

        JTable table = new JTable(model);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setForeground(new Color(0, 120, 215)); // Blu per l'aggiunta
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        // Editor che apre il dialog di conferma finale
        table.getColumnModel().getColumn(1).setCellEditor(new AddMemberActionCellEditor(mainFrame, table, teamId, this));

        resultsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}