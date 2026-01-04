package frontend.gui;

import frontend.controller.AuthController;

import javax.swing.*;

public class LogOutButton extends IconButton {

    public LogOutButton(JFrame mainFrame) {

        super("/frontend/gui/images/logOutButtonIcon.svg", 50, 50);

        setActionListener(mainFrame);
    }

    private void setActionListener(JFrame  mainFrame) {

        this.addActionListener(e -> {

            int response = JOptionPane.showConfirmDialog(mainFrame,
                    "Sei sicuro di voler uscire?", "Conferma LogOut",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {

                SwingUtilities.invokeLater(() -> {
                    AuthController.getInstance().setLoggedUser(null);
                    mainFrame.dispose();
                    new LogInPage().setVisible(true);
                });
            }
        });
    }
}
