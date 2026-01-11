package frontend.gui;

import javax.swing.*;

public class MenuButton extends IconButton {

    private final String[] options = {"Crea nuova utenza",  "Visualizza DashBoard", "Crea nuovo progetto"};

    public MenuButton() {

        super("/frontend/gui/images/menuIcon.svg", 50, 50);

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
            JFrame owner;
            switch (menuItem.getText()) {

                case "Crea nuova utenza":

                    owner = (JFrame) SwingUtilities.getWindowAncestor(this);

                    DevOrAdminCreationDialog devOrAdminCreationDialog = new DevOrAdminCreationDialog(owner);
                    devOrAdminCreationDialog.setVisible(true);
                    break;

                case "Visualizza DashBoard":

                    JDialog dialog = new DashBoard((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this));
                    dialog.pack();
                    dialog.setLocationRelativeTo(SwingUtilities.getAncestorOfClass(JFrame.class, this));
                    dialog.setVisible(true);
                    break;

                case "Crea nuovo progetto":

                    owner = (JFrame) SwingUtilities.getWindowAncestor(this);

                    CreateProjectDialog createProjectDialog = new CreateProjectDialog(owner);
                    createProjectDialog.setVisible(true);
                    break;
                default:
                    break;
            }
        });
    }
}
