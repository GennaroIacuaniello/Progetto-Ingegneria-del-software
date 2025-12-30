package frontend.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateTeamDialog extends JDialog {
    public CreateTeamDialog(JFrame owner, int projectId) {
        super(owner, "Nuovo Team", true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Inserisci il nome del nuovo team:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.WEST, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 10, 0));
        panel.add(label, Constraints.getGridBagConstraints());

        JTextField nameField = new JTextField(20);
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER, new Insets(0, 0, 20, 0));
        panel.add(nameField, Constraints.getGridBagConstraints());

        JButton confirmBtn = new JButton("Conferma");
        confirmBtn.setBackground(new Color(0, 120, 215));
        confirmBtn.setForeground(Color.WHITE);

        confirmBtn.addActionListener(e -> {
            String teamName = nameField.getText().trim();
            if (!teamName.isEmpty()) {
                // LOGICA: Chiamata al controller per il team collegato al progetto
                // TeamController.getInstance().createTeam(teamName, projectId);
                System.out.println("Team '" + teamName + "' creato per Progetto ID: " + projectId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Il nome del team è obbligatorio");
            }
        });

        Constraints.setConstraints(0, 2, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
        panel.add(confirmBtn, Constraints.getGridBagConstraints());

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
    }
}