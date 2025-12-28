package frontend.gui;

import frontend.controller.Controller;
import frontend.controller.ProjectController;

import javax.swing.*;

public class ProjectIconCellEditorDeveloper extends ProjectIconCellEditorUser {

    public ProjectIconCellEditorDeveloper(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        ProjectController.getInstance().setProject((Integer)parentTable.getValueAt(selectedRow, 0),
                (String)parentTable.getValueAt(selectedRow, 1));

        switch (action) {

            case "SEGNALA ISSUE":
                homePanel.setContentPanel(new ReportIssueDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                homePanel.setContentPanel(new SearchReportedIssuePageDeveloper(mainFrame, homePanel));
                break;

            case "ISSUE ASSEGNATE":
                homePanel.setContentPanel(new SearchAssignedIssuePage(mainFrame, homePanel));
                break;
        }

        return null;
    }
}
