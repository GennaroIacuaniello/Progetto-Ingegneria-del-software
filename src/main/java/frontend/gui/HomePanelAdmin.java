package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class HomePanelAdmin extends HomePanelDeveloper {

    protected HomePanelAdmin(JFrame mainFrame) {

        super(mainFrame);

        setMenuButton();
    }

    private void setMenuButton() {

        MenuButton menuButton = new MenuButton();

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                new Insets(10, 10, 0, 0));
        homePanel.add(menuButton, Constraints.getGridBagConstraints());
    }

    @Override
    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelAdmin(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }
}
