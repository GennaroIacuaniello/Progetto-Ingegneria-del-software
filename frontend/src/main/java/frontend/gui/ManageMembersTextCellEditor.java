package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Editor per la cella "Gestisci membri".
 * Gestisce l'evento di click sulla scritta testuale all'interno della tabella dei team.
 */
public class ManageMembersTextCellEditor extends DefaultCellEditor {
    private final JButton button;
    private String label;
    private final JFrame mainFrame;
    private final JTable table;

    public ManageMembersTextCellEditor(JFrame mainFrame, JTable table) {
        super(new JCheckBox()); // Costruttore richiesto da DefaultCellEditor
        this.mainFrame = mainFrame;
        this.table = table;

        // Configurazione del pulsante che fungerà da editor
        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false); // Rende il pulsante invisibile, lasciando solo il testo
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Stile coerente con AddNewFlightDialog
        this.button.setForeground(new Color(0, 120, 215));
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));

        this.button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // 1. Recupera l'ID del TEAM (dalla colonna 0 della tabella team)
                int teamId = (int) table.getValueAt(row, 0);

                // 2. Chiude l'editor per evitare che la cella resti "bloccata"
                fireEditingStopped();

                // 3. APERTURA EFFETTIVA: Istanzia il dialog dei membri passando l'ID del team
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