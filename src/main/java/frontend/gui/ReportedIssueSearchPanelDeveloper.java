package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class ReportedIssueSearchPanelDeveloper extends ReportedIssueSearchPanelUser {

    private JComboBox<String> priorityComboBox;
    private static final String[] priorityOptions = {"Tutte", "Molto bassa", "Bassa", "Media", "Alta", "Molto alta"};

    public ReportedIssueSearchPanelDeveloper(JFrame mainFrame) {

        super(mainFrame);

        setPriorityComboBox();
    }

    public void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(priorityOptions);

        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(3, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    @Override
    protected void searchButtonActionListener() {
        //todo implementa
    }
}
