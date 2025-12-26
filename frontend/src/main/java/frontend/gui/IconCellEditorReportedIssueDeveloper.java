package frontend.gui;

import javax.swing.*;

public class IconCellEditorReportedIssueDeveloper extends IconCellEditorReportedIssueUser{

    public IconCellEditorReportedIssueDeveloper(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(mainFrame, url, width, height, table);
    }

    @Override
    public Object getCellEditorValue() {

        //todo: implementa
        System.out.println("forse funziona");

        return null;
    }
}
