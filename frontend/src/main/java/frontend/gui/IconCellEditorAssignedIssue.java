package frontend.gui;

import javax.swing.*;

public class IconCellEditorAssignedIssue extends IconCellEditorReportedIssueDeveloper {

    public IconCellEditorAssignedIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        //todo: implementa
        new ShowAssignedIssue(mainFrame);

        return null;
    }
}
