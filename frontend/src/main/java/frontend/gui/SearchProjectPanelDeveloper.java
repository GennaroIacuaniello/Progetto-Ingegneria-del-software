package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchProjectPanelDeveloper extends SearchProjectPanelUser {

    public SearchProjectPanelDeveloper(JFrame mainFrame, HomePanelDeveloper homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        SearchProjectsButtonUser searchButton = new SearchProjectsButtonDeveloper(mainFrame, homePanel, searchTextField, TEXTFIELD_PLACEHOLDER);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        searchProjectPanel.add(searchButton, Constraints.getGridBagConstraints());
    }
}
