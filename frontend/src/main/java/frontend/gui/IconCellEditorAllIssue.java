package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;

public class IconCellEditorAllIssue extends IconCellEditorAssignedIssue{

    public IconCellEditorAllIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        ControllerTMP.setIssue(ControllerTMP.getIssueFromIndex(parentTable.getSelectedRow()));
        ControllerTMP.setIssueDetails();

        new ShowIssueAdmin(mainFrame);

        return null;
    }
}
