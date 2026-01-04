package frontend.gui;

import frontend.controller.AuthController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogOutButton extends IconButton {

    public LogOutButton(JFrame mainFrame) {

        super("/frontend/gui/images/logOutButtonIcon.svg", 50, 50);

        setActionListener(mainFrame);
    }

    private void setActionListener(JFrame  mainFrame) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                AuthController.getInstance().setLoggedUser(null);
                new LogInPage();
                mainFrame.dispose();
            }
        });
    }
}
