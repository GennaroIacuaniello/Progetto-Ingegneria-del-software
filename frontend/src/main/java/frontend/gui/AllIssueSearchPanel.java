package frontend.gui;

import frontend.controller.ControllerTMP;

import javax.swing.*;

public class AllIssueSearchPanel extends ReportedIssueSearchPanelDeveloper {

    public AllIssueSearchPanel(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(mainFrame, searchPage);
    }

    @Override
    protected void searchButtonActionListener(JFrame mainFrame) {

        ControllerTMP.searchReportedIssues(titleTextField.getText(), TITLE_PLACEHOLDER, (String)statusComboBox.getSelectedItem(),
                tagsButton.getTags(), (String)typeComboBox.getSelectedItem(), (String)priorityComboBox.getSelectedItem());

        new AllIssueSearchResultsPanel(mainFrame, searchPage, ControllerTMP.getIssuesTitles());
    }
}
