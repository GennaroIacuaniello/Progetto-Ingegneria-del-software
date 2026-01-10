package frontend.gui;

import javax.swing.*;
import java.awt.*;


public class AddMemberActionCellEditor extends DefaultCellEditor {

    private final JButton button;
    private String label;

    public AddMemberActionCellEditor(JFrame mainFrame, JTable table, AddMemberDialog parentDialog) {
        super(new JCheckBox());

        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setForeground(new Color(0, 120, 215));
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                String email = (String) table.getValueAt(row, 0);

                fireEditingStopped();


                ConfirmAddDialog confirm = new ConfirmAddDialog(mainFrame, email, parentDialog);
                confirm.setVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}