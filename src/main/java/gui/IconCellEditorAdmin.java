package gui;

import controller.Controller;

import javax.swing.*;

public class IconCellEditorAdmin extends IconCellEditorDeveloper{

    public IconCellEditorAdmin(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        String projectId = (String)parentTable.getValueAt(selectedRow, 0);

        switch (action) {

            case "SEGNALA ISSUE":
                Controller.setProject(projectId);
                homePanel.setContentPanel(new ReportIssueDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                break;

            case "ISSUE ASSEGNATE":
                break;

            case "VEDI TUTTE LE ISSUE":
                break;
        }

        return null;
    }
}
