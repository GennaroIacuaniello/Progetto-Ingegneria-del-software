package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;
import java.awt.*;

public class ShowReportedIssueDeveloper extends ShowReportedIssueUser {

    public ShowReportedIssueDeveloper(JFrame parent) {

        super(parent);
    }

    @Override
    protected void setUpperPanel() {

        upperPanel = new RoundedPanel(new GridBagLayout());

        upperPanel.setBackground(ColorsList.EMPTY_COLOR);
        upperPanel.setBorder(BorderFactory.createEmptyBorder());

        setBackButton();
        setPriorityLabel();

        Constraints.setConstraints(0, 0, 4, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER);
        mainPanel.add(upperPanel, Constraints.getGridBagConstraints());
    }

    private void setPriorityLabel() {

        JLabel priorityLabel = new JLabel(ControllerTMP.getIssuePriority());
        priorityLabel.setBorder(BorderFactory.createEmptyBorder());
        priorityLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(priorityLabel);

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, new Insets(5, 5, 5, 5));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }
}
