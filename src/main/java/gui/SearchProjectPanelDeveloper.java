package gui;

import javax.swing.*;

public class SearchProjectPanelDeveloper extends SearchProjectPanelUser {

    public SearchProjectPanelDeveloper(JFrame mainFrame, HomePanelDeveloper homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        SearchButtonUser searchButton = new SearchButtonDeveloper(mainFrame, homePanel);

        searchProjectPanel.add(searchButton);
    }
}
