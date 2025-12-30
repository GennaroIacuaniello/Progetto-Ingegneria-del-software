package frontend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfirmDeleteMemberDialog extends JDialog {

    // Aggiunto teamId al costruttore per permettere la rimozione effettiva nel DB
    public ConfirmDeleteMemberDialog(JFrame owner, String email, int teamId, ManageMembersDialog parentDialog) {
        super(owner, "Conferma Rimozione", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        Constraints constraints = new Constraints();

        JLabel label = new JLabel("Rimuovere l'utente " + email + " dal team?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        constraints.setConstraints(0, 0, 2, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(label, constraints.getGridBagConstraints());

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(220, 53, 69));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Unico ActionListener che gestisce tutto il processo
        confirmBtn.addActionListener(e -> {
            // 1. Chiamata al controller (quando la implementerai)
            // teamController.removeMemberFromTeam(teamId, email);

            System.out.println("Rimosso utente " + email + " dal team " + teamId);

            // 2. Chiamata al metodo del padre per ricaricare i dati dal DB
            parentDialog.performSearch();

            // 3. Chiude il dialog di conferma
            dispose();
        });

        constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5));
        panel.add(confirmBtn, constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }
}