package gui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;

class IconCellEditor extends DefaultCellEditor {

    protected IconButton button;
    private int selectedRow;
    private final JTable parentTable;

    public IconCellEditor(String url, int width, int height, JTable table) {

        super(new JTextField()); //se togli il parametro si incazza

        this.parentTable = table;

        setClickCountToStart(1);

        button = new IconButton(url, width, height);


        button.addActionListener(e -> {
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        this.selectedRow = row;

       /* if (isSelected) {
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setBackground(table.getBackground());
        }*/

        return button;
    }

    @Override
    public Object getCellEditorValue() {

        int columnIndex = parentTable.getEditingColumn();

        String action = parentTable.getColumnName(columnIndex);

        String projectId = (String)parentTable.getValueAt(selectedRow, 0);

        System.out.println("Azione: " + action + ", ID: " + projectId);

        //todo: implementa in base a action e id

        return null;
    }
}