package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ReportIssue extends RoundedPanel{

    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JOptionPane typeOptionPane;
    private JButton tagsButton;
    private JFileChooser fileChooser;
    private JButton reportButton;
    private JButton cancelButton;
    private static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String DESCRIPTION_PLACEHOLDER = "Inserisci descrizione";

    public ReportIssue() {

        super(new GridBagLayout());

        setRoundedPanel();
        setTitleTextField();
        setDescriptionTextArea();

        setVisible(true);
    }

    private void setRoundedPanel() {

        setRoundBorderColor(ColorsList.EMPTY_COLOR);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setTitleTextField() {

        titleTextField = new JTextField(TITLE_PLACEHOLDER, 30);
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        setFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        RoundedPanel tmpPanel = createRoundedPanelContainer(titleTextField);

        Constraints.setConstraints(0, 1, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LAST_LINE_START,
                0.5f, 0.5f, new Insets(10, 10, 0, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setDescriptionTextArea() {

        descriptionTextArea = new JTextArea(DESCRIPTION_PLACEHOLDER, 10, 60);
        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());

        setFocusBehaviour(descriptionTextArea, DESCRIPTION_PLACEHOLDER);

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);
        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(ColorsList.EMPTY_COLOR);
        tmpScrollPane.setViewportView(descriptionTextArea);

        RoundedPanel tmpPanel = createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                1f, 1f, new Insets(0, 10, 10, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private RoundedPanel createRoundedPanelContainer(Component component) {

        RoundedPanel tmpPanel = new RoundedPanel(new GridBagLayout());

        tmpPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                0.01f, 0.01f, new Insets(5, 5, 5, 5));
        tmpPanel.add(component, Constraints.getGridBagConstraints());

        return tmpPanel;
    }

    private void setFocusBehaviour(JTextComponent component, String placeHolder) {

        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (component.getText().equals(placeHolder)) {
                    component.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (component.getText().isEmpty()) {
                    component.setText(placeHolder);
                }
            }
        });
    }
}
