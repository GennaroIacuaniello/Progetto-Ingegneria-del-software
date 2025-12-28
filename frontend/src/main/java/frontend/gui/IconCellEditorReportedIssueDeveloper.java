package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;

public class IconCellEditorReportedIssueDeveloper extends IconCellEditorReportedIssueUser{

    public IconCellEditorReportedIssueDeveloper(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        ControllerTMP.setIssue(ControllerTMP.getIssueFromIndex(parentTable.getSelectedRow()));
        ControllerTMP.setIssueDetails();

        new ShowReportedIssueDeveloper(mainFrame);

        return null;
    }
}
