package frontend.gui;

import frontend.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchProjectsButtonUser extends IconButton {

    public SearchProjectsButtonUser(JFrame mainFrame, HomePanelUser homePanel, JTextField searchTextField, String placeholder) {

        super("/frontend/gui/images/searchButton.png", 30, 30);

        setActionListener(mainFrame, homePanel, searchTextField, placeholder);
    }

    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel,  JTextField searchTextField, String placeholder) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Controller.searchProjects(searchTextField.getText(), placeholder);

                new SearchProjectResultsUser(mainFrame, homePanel, Controller.getProjectsIds(), Controller.getProjectsNames());
            }
        });
    }
}
