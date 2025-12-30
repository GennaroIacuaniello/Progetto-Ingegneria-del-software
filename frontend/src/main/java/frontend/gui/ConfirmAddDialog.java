package frontend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfirmAddDialog extends JDialog {

    public ConfirmAddDialog(JFrame owner, String email, int teamId, AddMemberDialog parentDialog) {
        super(owner, "Conferma Aggiunta", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Aggiungere " + email + " al team?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(40, 167, 69));
        confirmBtn.setForeground(Color.WHITE);

        confirmBtn.addActionListener(e -> {

            System.out.println("Utente aggiunto: " + email);


            parentDialog.dispose();
            dispose();
        });

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }
}