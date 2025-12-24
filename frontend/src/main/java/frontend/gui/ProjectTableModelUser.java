package frontend.gui;

import javax.swing.table.AbstractTableModel;

public class ProjectTableModelUser extends AbstractTableModel {

    protected String[] columnNames;
    protected final Object[][] data;

    public ProjectTableModelUser(Object[][] data) {

        setColumnNames();
        this.data = data;
    }

    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE"};
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

        if (columnIndex >= 2 && columnIndex <= 3)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 3;
    }
}