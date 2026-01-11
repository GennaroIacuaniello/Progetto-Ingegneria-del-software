package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;

public class IconCellEditorAllIssue extends IconCellEditorAssignedIssue{

    public IconCellEditorAllIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        boolean success = IssueController.getInstance().getIssueById();

        if(!success)
            return null;

        ShowReportedIssueUser dialog = new ShowIssueAdmin(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}
