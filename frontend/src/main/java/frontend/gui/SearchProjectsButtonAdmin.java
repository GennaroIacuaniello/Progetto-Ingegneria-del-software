package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

public class SearchProjectsButtonAdmin extends SearchProjectsButtonDeveloper {

    public SearchProjectsButtonAdmin(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super(mainFrame, homePanel, searchTextField, placeholder);
    }

    @Override
    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        new SearchProjectResultsAdmin(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}
