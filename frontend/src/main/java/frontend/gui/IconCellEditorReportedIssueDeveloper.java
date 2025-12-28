package frontend.gui;

import frontend.controller.ControllerTMP;
import frontend.controller.IssueController;

import javax.swing.*;

public class IconCellEditorReportedIssueDeveloper extends IconCellEditorReportedIssueUser{

    public IconCellEditorReportedIssueDeveloper(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        ControllerTMP.setIssueDetails();

        new ShowReportedIssueDeveloper(mainFrame);

        return null;
    }
}
