package frontend.gui;

import frontend.controller.Controller;

import javax.swing.*;

public class IconCellEditorAdmin extends IconCellEditorDeveloper{

    public IconCellEditorAdmin(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        Integer projectId = (Integer)parentTable.getValueAt(selectedRow, 0);

        switch (action) {

            case "SEGNALA ISSUE":
                Controller.setProject(projectId, "Ciao");
                homePanel.setContentPanel(new ReportIssueDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                homePanel.setContentPanel(new SearchReportedIssuePageDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE ASSEGNATE":
                //todo: controller
                homePanel.setContentPanel(new SearchAssignedIssuePage(mainFrame, homePanel));
                break;

            case "VEDI TUTTE LE ISSUE":
                //todo: controller
                homePanel.setContentPanel(new SearchAllIssuePage(mainFrame, homePanel));
                break;
        }

        return null;
    }
}
