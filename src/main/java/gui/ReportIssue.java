package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ReportIssue extends RoundedPanel{

    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JComboBox<String> typeOptionPane;
    private JButton tagsButton;
    private JFileChooser fileChooser;
    private JButton reportButton;
    private JButton cancelButton;
    private static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String DESCRIPTION_PLACEHOLDER = "Inserisci descrizione";
    private static final String[] options = {"Bug", "Documentation", "Feature", "Question"};

    public ReportIssue(JFrame mainFrame) {

        super(new GridBagLayout());

        setRoundedPanel();
        setTitleTextField();
        setDescriptionTextArea();
        setTypeOptionPane();
        setTagsButton(mainFrame);

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

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(titleTextField);

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

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                1f, 1f, new Insets(0, 10, 10, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setTypeOptionPane() {

        typeOptionPane = new JComboBox<>(options);
        typeOptionPane.setBorder(BorderFactory.createEmptyBorder());
        typeOptionPane.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeOptionPane);

        Constraints.setConstraints(0, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        Constraints.setConstraints(1, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tagsButton, Constraints.getGridBagConstraints());
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
