package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDialog extends JDialog {

    protected RoundedPanel mainPanel;
    protected RoundedPanel contentPanel;
    protected RoundedPanel buttonsPanel;
    private IconButton confirmButton;

    public MyDialog(JFrame owner) {

        super(owner, true);

        setDialog();
        setMainPanel();
        setContentPanel();
        setButtonsPanel();
        setOkButton();

        pack();
    }

    private void setDialog() {

        setUndecorated(true);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setMainPanel() {

        mainPanel = new RoundedPanel(new GridBagLayout());

        mainPanel.setBackground(ColorsList.BACKGROUND_COLOR);
        mainPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        setContentPane(mainPanel);
    }

    private void setContentPanel() {

        contentPanel = new RoundedPanel(new GridBagLayout());

        contentPanel.setBackground(ColorsList.EMPTY_COLOR);
        contentPanel.setRoundBorderColor(ColorsList.EMPTY_COLOR);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER);
        mainPanel.add(contentPanel, Constraints.getGridBagConstraints());
    }

    private void setButtonsPanel() {

        buttonsPanel = new RoundedPanel(new GridBagLayout());

        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        mainPanel.add(buttonsPanel, Constraints.getGridBagConstraints());
    }

    private void setOkButton() {

        confirmButton = new IconButton("/frontend/gui/images/confirmButtonIcon.png", 30, 30);

        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        buttonsPanel.add(confirmButton, Constraints.getGridBagConstraints());
    }
}
