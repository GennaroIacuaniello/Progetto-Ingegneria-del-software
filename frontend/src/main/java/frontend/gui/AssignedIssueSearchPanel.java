package frontend.gui;

import frontend.controller.ControllerTMP;
import frontend.controller.IssueController;

import javax.swing.*;
import java.util.Objects;

public class AssignedIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    public AssignedIssueSearchPanel(JFrame mainFrame,  SearchReportedIssuePageUser searchPage) {
        super(mainFrame, searchPage);
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        IssueController.getInstance().searchAssignedIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                (Objects.equals(statusComboBox.getSelectedItem(), ALL_PLACEHOLDER) ? null : (String)statusComboBox.getSelectedItem()),
                tagsButton.getTags(), (Objects.equals(typeComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)typeComboBox.getSelectedItem(),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem());

        new AssignedIssueSearchResultsPanel(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}
