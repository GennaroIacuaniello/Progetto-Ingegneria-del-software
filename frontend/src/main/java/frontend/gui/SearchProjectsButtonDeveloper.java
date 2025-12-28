package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchProjectsButtonDeveloper extends SearchProjectsButtonUser {

    public SearchProjectsButtonDeveloper(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super(mainFrame, homePanel, searchTextField, placeholder);
    }

    @Override
    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ProjectController.getInstance().searchProjectsByName(searchTextField.getText());

                new SearchProjectResultsDeveloper(mainFrame, homePanel, ProjectController.getInstance().getProjectsIds(), ProjectController.getInstance().getProjectsNames());
            }
        });
    }
}
