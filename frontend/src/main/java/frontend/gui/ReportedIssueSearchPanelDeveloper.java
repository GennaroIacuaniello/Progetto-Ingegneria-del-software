package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ReportedIssueSearchPanelDeveloper extends ReportedIssueSearchPanelUser {

    protected JComboBox<String> priorityComboBox;
    private static final String[] priorityOptions = {"Tutte", "Molto bassa", "Bassa", "Media", "Alta", "Molto alta"};

    public ReportedIssueSearchPanelDeveloper(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);

        setPriorityComboBox();
    }

    public void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(priorityOptions);

        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(3, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        IssueController.getInstance().searchReportedIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                (Objects.equals(statusComboBox.getSelectedItem(), ALL_PLACEHOLDER) ? null : (String)statusComboBox.getSelectedItem()),
                tagsButton.getTags(), (Objects.equals(typeComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)typeComboBox.getSelectedItem(),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem());

        new ReportedIssueSearchResultsPanelDeveloper(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}
