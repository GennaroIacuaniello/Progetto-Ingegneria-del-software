package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Editor per la cella "Aggiungi" nella tabella di ricerca nuovi utenti.
 */
public class AddMemberActionCellEditor extends DefaultCellEditor {
    private final JButton button;
    private final JFrame mainFrame;
    private final JTable table;
    private final int teamId;
    private final AddMemberDialog parentDialog;

    public AddMemberActionCellEditor(JFrame mainFrame, JTable table, int teamId, AddMemberDialog parentDialog) {
        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;
        this.teamId = teamId;
        this.parentDialog = parentDialog;

        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setForeground(new Color(0, 120, 215)); // Blu per l'aggiunta
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // Recupera l'email dell'utente selezionato dalla prima colonna
                String email = (String) table.getValueAt(row, 0);

                fireEditingStopped(); // Chiude l'editor nella tabella

                // Apre il dialog di conferma finale
                ConfirmAddDialog confirm = new ConfirmAddDialog(mainFrame, email, teamId, parentDialog);
                confirm.setVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value != null ? value.toString() : "");
        return button;
    }
}