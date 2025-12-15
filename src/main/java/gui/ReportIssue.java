package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ReportIssue extends RoundedPanel{

    private JLabel titleLabel;
    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JOptionPane typeOptionPane;
    private JButton tagsButton;
    private JFileChooser fileChooser;
    private JButton reportButton;
    private JButton cancelButton;
    private static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String DESCRIPTION_PLACEHOLDER = "Inserisci descrizione";
    private final Color BorderColor = new Color (77, 133, 255);

    public ReportIssue() {

        super(new GridBagLayout());

        setRoundedPanel();
        //setTitleLabel();
        setTitleTextField();
        setDescriptionTextArea();

        setVisible(true);
    }

    /*private void setTitleLabel() {

        titleLabel = new JLabel ("SEGNALA ISSUE");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));

        titleLabel.setBackground(new Color(0, 0, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder());

        Constraints.setConstraints(0, 0, 4, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.PAGE_START, 0.01f, 0.01f);
        contentPanel.add(titleLabel, Constraints.getGridBagConstraints());
    }*/

    private void setRoundedPanel() {

        setRoundBorderColor(new Color(0, 0, 0, 0));
        setBackground(Color.WHITE);
    }

    private void setTitleTextField() {

        RoundedPanel tmpPanel = new RoundedPanel(new GridBagLayout());
        tmpPanel.setRoundBorderColor(BorderColor);
        tmpPanel.setBackground(new Color(0, 0, 0, 0));

        titleTextField = new JTextField(TITLE_PLACEHOLDER, 30);
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        titleTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (titleTextField.getText().equals(TITLE_PLACEHOLDER)) {
                    titleTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (titleTextField.getText().isEmpty()) {
                    titleTextField.setText(TITLE_PLACEHOLDER);
                }
            }
        });

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.01f, 0.01f, new Insets(5, 5, 5, 5));
        tmpPanel.add(titleTextField, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 1, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LAST_LINE_START,
                0.5f, 0.5f, new Insets(10, 10, 0, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setDescriptionTextArea() {

        RoundedPanel tmpPanel = new RoundedPanel(new GridBagLayout());
        tmpPanel.setRoundBorderColor(BorderColor);
        tmpPanel.setBackground(new Color(0, 0, 0, 0));

        descriptionTextArea = new JTextArea(DESCRIPTION_PLACEHOLDER, 10, 60);
        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());

        descriptionTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER)) {
                    descriptionTextArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (descriptionTextArea.getText().isEmpty()) {
                    descriptionTextArea.setText(DESCRIPTION_PLACEHOLDER);
                }
            }
        });

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);
        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(new Color(0, 0, 0, 0));
        tmpScrollPane.setViewportView(descriptionTextArea);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.01f, 0.01f, new Insets(5, 5, 5, 5));
        tmpPanel.add(tmpScrollPane, Constraints.getGridBagConstraints());

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                1f, 1f, new Insets(0, 10, 10, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}
