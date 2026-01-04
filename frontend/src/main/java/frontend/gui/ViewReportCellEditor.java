package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;

public class ViewReportCellEditor extends DefaultCellEditor {
    private final JButton button;
    private final JFrame mainFrame;
    private final JTable table;

    public ViewReportCellEditor(JFrame mainFrame, JTable table, HomePanelUser homePanel) {
        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;
        this.button = new JButton();

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                TeamController.getInstance().setTeamWithId((int) table.getValueAt(row, 0));

                fireEditingStopped();

                //TODO: vedi come aprire questa pagina

                homePanel.setContentPanel(new TeamReportPage(homePanel));

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
        button.setText(v.toString());
        return button;
    }
}