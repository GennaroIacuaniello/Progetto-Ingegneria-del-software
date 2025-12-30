package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class RemoveMemberCellEditor extends DefaultCellEditor {
    private final JButton button;
    private final JFrame mainFrame;
    private final JTable table;
    private final ManageMembersDialog parentDialog;

    public RemoveMemberCellEditor(JFrame mainFrame, JTable table, ManageMembersDialog parentDialog) {

        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;
        this.parentDialog = parentDialog;
        this.button = new JButton();

        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setForeground(new Color(220, 53, 69));
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String email = (String) table.getValueAt(row, 0);
                fireEditingStopped();

                ConfirmDeleteMemberDialog confirm = new ConfirmDeleteMemberDialog(mainFrame, email, parentDialog);
                confirm.setVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
        button.setText(v.toString());
        return button;
    }
}