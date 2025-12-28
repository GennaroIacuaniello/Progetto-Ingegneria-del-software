package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;

public class AllIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    public AllIssueSearchPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        ControllerTMP.searchAllIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                (String)statusComboBox.getSelectedItem(), tagsButton.getTags(), (String)typeComboBox.getSelectedItem(),
                (String)priorityComboBox.getSelectedItem());

        new AllIssueSearchResultsPanel(mainFrame, searchPage, ControllerTMP.getIssuesTitles());
    }
}
