package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ManageMembersTextCellEditor extends DefaultCellEditor {
    private final JButton button;
    private String label;
    private final JFrame mainFrame;
    private final JTable table;

    public ManageMembersTextCellEditor(JFrame mainFrame, JTable table) {
        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;


        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));


        this.button.setForeground(new Color(0, 120, 215));
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));

        this.button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                int teamId = (int) table.getValueAt(row, 0);


                fireEditingStopped();

                ManageMembersDialog membersDialog = new ManageMembersDialog(mainFrame, teamId);
                membersDialog.setVisible(true);
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