package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;

public class ViewReportCellEditor extends DefaultCellEditor {
    private final JButton button;

    private String label;

    public ViewReportCellEditor(JTable table, HomePanelUser homePanel, ManageTeamsPanel manageTeamsPanel) {
        super(new JCheckBox());
        this.button = new JButton();

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                TeamController.getInstance().setTeamWithId((int) table.getValueAt(row, 0));

                fireEditingStopped();


                homePanel.setContentPanel(new TeamReportPage(manageTeamsPanel));

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}