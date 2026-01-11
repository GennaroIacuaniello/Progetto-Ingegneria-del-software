package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.util.Objects;

public class AllIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    public AllIssueSearchPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        boolean success = IssueController.getInstance().searchAllIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())), tagsButton.getTags(), formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())),
                (Objects.equals(priorityComboBox.getSelectedItem(), ALL_PLACEHOLDER)) ? null : (String)priorityComboBox.getSelectedItem());

        if(!success)
            return;

        new AllIssueSearchResultsPanel(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }
}
