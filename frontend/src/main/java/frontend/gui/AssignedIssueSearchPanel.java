package frontend.gui;

import frontend.controller.ControllerTMP;
import frontend.controller.IssueController;

import javax.swing.*;

public class AssignedIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    public AssignedIssueSearchPanel(JFrame mainFrame,  SearchReportedIssuePageUser searchPage) {
        super(mainFrame, searchPage);
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        IssueController.getInstance().searchAssignedIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                (String)statusComboBox.getSelectedItem(), tagsButton.getTags(), (String)typeComboBox.getSelectedItem(),
                (String)priorityComboBox.getSelectedItem());

        new AssignedIssueSearchResultsPanel(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}
