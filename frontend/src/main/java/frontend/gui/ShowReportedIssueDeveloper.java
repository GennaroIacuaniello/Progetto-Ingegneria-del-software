package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;

public class ShowReportedIssueDeveloper extends ShowReportedIssueUser {

    public ShowReportedIssueDeveloper(JFrame parent) {

        super(parent);

        setPriorityLabel();
    }

    private void setPriorityLabel() {

        JLabel priorityLabel = new JLabel("Priorità: " + IssueController.getInstance().getIssuePriority());
        priorityLabel.setBorder(BorderFactory.createEmptyBorder());
        priorityLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(priorityLabel);

        Constraints.setConstraints(3, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, new Insets(5, 5, 5, 60));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }
}
