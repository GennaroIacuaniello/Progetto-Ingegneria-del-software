package frontend.gui;

import frontend.controller.Controller;

import javax.swing.*;
import java.awt.*;

class ProjectIconCellEditorUser extends DefaultCellEditor {

    private IconButton button;
    protected int selectedRow;
    protected final JTable parentTable;
    protected HomePanelUser homePanel;
    protected JFrame  mainFrame;

    public ProjectIconCellEditorUser(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

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

        Integer projectId = (Integer)parentTable.getValueAt(selectedRow, 0);

        switch (action) {

            case "SEGNALA ISSUE":
                Controller.setProject(projectId, "Ciao");
                homePanel.setContentPanel(new ReportIssueUser(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                //todo: controller
                homePanel.setContentPanel(new SearchReportedIssuePageUser(mainFrame, homePanel));
                break;
        }

        return null;
    }
}