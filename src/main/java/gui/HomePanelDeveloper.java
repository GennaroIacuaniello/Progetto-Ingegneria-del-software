package gui;

import javax.swing.*;
import java.awt.*;

public class HomePanelDeveloper extends HomePanelUser{

    public HomePanelDeveloper(JFrame mainFrame) {

        super(mainFrame);
    }

    @Override
    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelDeveloper(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }
}
