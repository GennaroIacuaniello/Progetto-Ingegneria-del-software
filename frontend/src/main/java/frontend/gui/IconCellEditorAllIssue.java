package frontend.gui;

import javax.swing.*;

public class IconCellEditorAllIssue extends IconCellEditorAssignedIssue{

    public IconCellEditorAllIssue(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        //todo: implementa
        new ShowIssueAdmin(mainFrame);

        return null;
    }
}
