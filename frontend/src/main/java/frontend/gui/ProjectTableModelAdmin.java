package frontend.gui;

public class ProjectTableModelAdmin extends ProjectTableModelDeveloper{

    public ProjectTableModelAdmin(Object[][] data) {
        super(data);
    }

    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE", "ISSUE ASSEGNATE", "VEDI TUTTE LE ISSUE", "VEDI TEAMS"};
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 6)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 6;
    }
}
