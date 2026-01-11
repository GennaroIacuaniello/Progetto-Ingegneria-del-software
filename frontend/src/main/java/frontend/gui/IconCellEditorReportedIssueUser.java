package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;

public class IconCellEditorReportedIssueUser extends DefaultCellEditor {

    private final IconButton button;
    protected final JTable parentTable;
    protected JFrame  mainFrame;

    public IconCellEditorReportedIssueUser(JFrame mainFrame, String url, int width, int height, JTable table) {

        super(new JTextField()); //se togli il parametro si incazza

        this.parentTable = table;
        this.mainFrame = mainFrame;

        setClickCountToStart(1);

        button = new IconButton(url, width, height);


        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        if (isSelected)
            button.setBackground(table.getSelectionBackground());
        else
            button.setBackground(table.getBackground());

        return button;
    }

    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        boolean success = IssueController.getInstance().getIssueById();

        ShowReportedIssueUser dialog = new ShowReportedIssueUser(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}
