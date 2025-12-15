package gui;

import java.awt.*;

public class HomePanelAdmin extends HomePanel{

    protected HomePanelAdmin() {

        super();

        setMenuButton();
    }

    private void setMenuButton() {

        MenuButton menuButton = new MenuButton();

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                new Insets(10, 10, 0, 0));
        homePanel.add(menuButton, Constraints.getGridBagConstraints());
    }
}
