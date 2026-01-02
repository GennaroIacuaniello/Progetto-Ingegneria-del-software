package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

public class ProjectIconCellEditorAdmin extends ProjectIconCellEditorDeveloper {

    public ProjectIconCellEditorAdmin(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        super(mainFrame, homePanel, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        //ProjectController.getInstance().setProjectWithValues((Integer)parentTable.getValueAt(selectedRow, 0),
        //        (String)parentTable.getValueAt(selectedRow, 1));

        ProjectController.getInstance().setProjectWithId((Integer)parentTable.getValueAt(selectedRow, 0));

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

            case "VEDI TUTTE LE ISSUE":
                homePanel.setContentPanel(new SearchAllIssuePage(mainFrame, homePanel));
                break;

            case "GESTISCI TEAM":

                ManageTeamsDialog teamsDialog = new ManageTeamsDialog(mainFrame);
                teamsDialog.setVisible(true);
                System.out.println("vedi teams");
                break;
        }

        return null;
    }
}
