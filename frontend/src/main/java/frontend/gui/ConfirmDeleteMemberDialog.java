package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmDeleteMemberDialog extends JDialog {

    static final Logger logger = Logger.getLogger(ConfirmDeleteMemberDialog.class.getName());


    public ConfirmDeleteMemberDialog(JFrame owner, String email, ManageMembersDialog parentDialog) {

        super(owner, "Conferma Rimozione", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Rimuovere l'utente " + email + " dal team?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Constraints.setConstraints(0, 0, 2, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JButton confirmBtn = getConfirmBtn(email, parentDialog);

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }

    private JButton getConfirmBtn(String email, ManageMembersDialog parentDialog) {

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(220, 53, 69));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));


        confirmBtn.addActionListener(e -> {

            boolean success = TeamController.getInstance().removeMemberFromSelectedTeam(email);

            if(!success)
                return;

            logger.log(Level.FINE, "Rimosso utente: {0}, dal team con ID: {1}", new Object[]{email, TeamController.getInstance().getTeam().getId()});

            parentDialog.performSearch();

            dispose();
        });
        return confirmBtn;
    }
}