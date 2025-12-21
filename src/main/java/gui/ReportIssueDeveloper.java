package gui;

import javax.swing.*;
import java.awt.*;

public class ReportIssueDeveloper extends ReportIssueUser {

    private JComboBox<String> priorityComboBox;
    private static final String[] options = {"Molto bassa", "Bassa", "Normale", "Alta", "Molto alta"};

    public ReportIssueDeveloper(JFrame mainFrame) {

        super(mainFrame);

        setPriorityComboBox();
    }
    
    private void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(options);
        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(3, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}
