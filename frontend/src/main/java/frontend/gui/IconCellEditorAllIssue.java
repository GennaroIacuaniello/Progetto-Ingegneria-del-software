package frontend.gui;

import frontend.controller.ControllerTMP;
import frontend.controller.IssueController;

import javax.swing.*;

public class IconCellEditorAllIssue extends IconCellEditorAssignedIssue{

    public IconCellEditorAllIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        IssueController.getInstance().setIssueDetails();

        new ShowIssueAdmin(mainFrame);

        return null;
    }
}
