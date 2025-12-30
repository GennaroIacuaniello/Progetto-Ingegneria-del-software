package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ManageTeamTextCellEditor extends DefaultCellEditor {
    private final JButton button;
    private String label;
    private final JFrame mainFrame;
    private final JTable table;

    public ManageTeamTextCellEditor(JFrame mainFrame, JTable table) {
        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;

        button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(new Color(0, 120, 215)); // Colore blu coerente
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int projectId = (int) table.getValueAt(row, 0);

                fireEditingStopped();

                ManageTeamsDialog teamsDialog = new ManageTeamsDialog(mainFrame, projectId);
                teamsDialog.setVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}