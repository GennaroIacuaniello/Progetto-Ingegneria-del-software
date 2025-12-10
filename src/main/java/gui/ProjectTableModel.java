package gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;

class ProjectTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE", "ISSUE ASSEGNATE", "VEDI TUTTE LE ISSUE"};
    private final Object[][] data;

    public ProjectTableModel(Object[][] data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int columnName) {
        return columnNames[columnName];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 5)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 5;
    }
}