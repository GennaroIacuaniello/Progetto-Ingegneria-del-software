package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends IconButton {

    private final String[] options = {"Crea nuova utenza",  "Visualizza DashBoard", "Visualizza Report", "Gestisci progetti"};

    public MenuButton() {

        super("/frontend/gui/images/menuIcon.png", 50, 50);

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

                case "Gestisci progetti":
                    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                    ManageProjectsDialog manageProjectsDialog = new ManageProjectsDialog(owner);
                    manageProjectsDialog.setVisible(true);
                default:
                    break;
            }
        });
    }
}
