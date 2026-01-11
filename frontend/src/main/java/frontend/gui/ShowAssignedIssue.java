package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;

public class ShowAssignedIssue extends ShowReportedIssueDeveloper {

    private JButton resolveButton;
    private RoundedPanel tmpPanel;

    public ShowAssignedIssue(JFrame parent) {

        super(parent);

        if (statusLabel.getText().equals("Stato: ASSIGNED"))
            setResolveButton();
    }
    
    private void setResolveButton() {

        resolveButton = new JButton("Segna come risolta");

        resolveButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        resolveButton.setBorder(BorderFactory.createEmptyBorder());
        resolveButton.setBackground(ColorsList.EMPTY_COLOR);

        resolveButton.addActionListener(e -> resolve());

        tmpPanel = ContainerFactory.createRoundedPanelContainer(resolveButton);

        tmpPanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
        tmpPanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(0, 5, 4, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void resolve() {

        boolean success = IssueController.getInstance().setIssueAsResolved();

        statusLabel.setText("Stato: Risolta");
        new FloatingMessage("Segnalazione avvenuta con successo", resolveButton, FloatingMessage.SUCCESS_MESSAGE);
        tmpPanel.setVisible(false);
    }


}
