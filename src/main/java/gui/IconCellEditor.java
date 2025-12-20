package gui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;

class IconCellEditor extends DefaultCellEditor {

    private IconButton button;
    private int selectedRow;
    private final JTable parentTable;
    private HomePanel homePanel;
    private JFrame  mainFrame;

    public IconCellEditor(JFrame mainFrame, HomePanel homePanel, String url, int width, int height, JTable table) {

        super(new JTextField()); //se togli il parametro si incazza

        this.parentTable = table;
        this.homePanel = homePanel;
        this.mainFrame = mainFrame;

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

        if (isSelected)
            button.setBackground(table.getSelectionBackground());
        else
            button.setBackground(table.getBackground());

       return button;
    }

    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        //String projectId = (String)parentTable.getValueAt(selectedRow, 0);

        switch (action) {

            case "SEGNALA ISSUE":
                homePanel.setContentPanel(new ReportIssue(mainFrame));
                break;

            case "ISSUE SEGNALATE":
                break;
        }

        return null;
    }
}