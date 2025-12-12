package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuButton extends IconButton {

    public MenuButton() {

        super("/gui/images/menuIcon.png", 50, 50);

        ArrayList<String> options = new ArrayList<>();
        options.add("Crea nuova utenza");
        options.add("Visualizza DashBoard");
        options.add("Visualizza Report");


        this.addActionListener(e -> {

            JPopupMenu popupMenu = new JPopupMenu();

            for (String option : options) {

                JMenuItem menuItem = new JMenuItem(option);

                menuItem.addActionListener(actionEvent -> {

                    switch (option) {

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

                popupMenu.add(menuItem);
            }

            popupMenu.show(this, 0, this.getHeight());
        });
    }
}
