package gui;

import javax.swing.*;

public class SearchProjectPanelAdmin extends SearchProjectPanelDeveloper{

    public SearchProjectPanelAdmin(JFrame mainFrame, HomePanelAdmin homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        SearchButtonUser searchButton = new SearchButtonAdmin(mainFrame, homePanel);

        searchProjectPanel.add(searchButton);
    }
}
