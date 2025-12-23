package frontend.gui;

public class ProjectTableModelDeveloper extends ProjectTableModelUser{

    public ProjectTableModelDeveloper(Object[][] data) {
        super(data);
    }

    @Override
    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE", "ISSUE ASSEGNATE"};
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 4)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 4;
    }
}
