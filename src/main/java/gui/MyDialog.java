package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDialog extends JDialog {

    private RoundedPanel mainPanel;
    protected RoundedPanel contentPanel;
    private IconButton confirmButton;
    private IconButton cancelButton;

    public MyDialog(JFrame owner) {

        super(owner, true);

        setDialog();
        setMainPanel();
        setContentPanel();
        setOkButton();
        setCancelButton();

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void setDialog() {

        setUndecorated(true);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setMainPanel() {

        mainPanel = new RoundedPanel(new GridBagLayout());

        mainPanel.setBackground(Color.WHITE);
        mainPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        setContentPane(mainPanel);
    }

    private void setContentPanel() {

        contentPanel = new RoundedPanel(new GridBagLayout());

        contentPanel.setBackground(ColorsList.EMPTY_COLOR);
        contentPanel.setRoundBorderColor(ColorsList.EMPTY_COLOR);

        Constraints.setConstraints(0, 0, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER);
        mainPanel.add(contentPanel, Constraints.getGridBagConstraints());
    }

    private void setOkButton() {

        confirmButton = new IconButton("/gui/images/confirmButtonIcon.png", 30, 30);

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        mainPanel.add(confirmButton, Constraints.getGridBagConstraints());
    }

    private void setCancelButton() {

        cancelButton = new IconButton("/gui/images/cancelButtonIcon.png", 30, 30);

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Constraints.setConstraints(1, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        mainPanel.add(cancelButton, Constraints.getGridBagConstraints());
    }
}
