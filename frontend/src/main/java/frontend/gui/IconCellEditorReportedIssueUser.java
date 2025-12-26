package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class IconCellEditorReportedIssueUser extends DefaultCellEditor {

    private IconButton button;
    protected int selectedRow;
    protected final JTable parentTable;
    protected HomePanelUser homePanel;
    protected JFrame  mainFrame;

    public IconCellEditorReportedIssueUser(JFrame mainFrame, String url, int width, int height, JTable table) {

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

        //todo: implementa
        System.out.println("forse funziona");

        return null;
    }
}
