package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;

public class SearchProjectsButtonUser extends IconButton {

    public SearchProjectsButtonUser(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super("/frontend/gui/images/searchButton.svg", 32, 32);

        setActionListener(mainFrame, homePanel, searchTextField, placeholder);
    }

    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        this.addActionListener(e -> search(mainFrame, homePanel, searchTextField, placeholder));

        searchTextField.addActionListener(e -> search(mainFrame, homePanel, searchTextField, placeholder));
    }

    protected void search(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        ProjectController.getInstance().searchProjectsByName((searchTextField.getText().equals(placeholder) ?
                "" : searchTextField.getText()));

        new SearchProjectResultsUser(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
    }
}
