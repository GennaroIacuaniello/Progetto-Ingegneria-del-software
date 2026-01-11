package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

public class SearchProjectsButtonDeveloper extends SearchProjectsButtonUser {

    public SearchProjectsButtonDeveloper(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super(mainFrame, homePanel, searchTextField, placeholder);
    }

    @Override
    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        boolean success = ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        if(!success)
            return;

        new SearchProjectResultsDeveloper(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}
