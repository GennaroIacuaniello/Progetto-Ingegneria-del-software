package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuButton extends IconButton {

    private final String[] options = {"Crea nuova utenza",  "Visualizza DashBoard", "Visualizza Report"};

    public MenuButton() {

        super("/gui/images/menuIcon.png", 50, 50);

        setActionListener();
    }

    private void setActionListener() {

        this.addActionListener(e -> {

            JPopupMenu popupMenu = new JPopupMenu();

            for (String option : options) {

                JMenuItem menuItem = new JMenuItem(option);

                setMenuItemActionListener(menuItem);

                popupMenu.add(menuItem);
            }

            popupMenu.show(this, 0, this.getHeight());
        });
    }

    private void setMenuItemActionListener(JMenuItem menuItem) {

        menuItem.addActionListener(actionEvent -> {

            switch (menuItem.getText()) {

                case "Crea nuova utenza":
                    System.out.println("Crea nuova utenza");
                    break;

                case "Visualizza DashBoard":
                    System.out.println("Visualizza DashBoard");
                    break;

                case "Visualizza Report":
                    System.out.println("Visualizza Report");
                    break;

                default:
                    break;
            }
        });
    }
}
