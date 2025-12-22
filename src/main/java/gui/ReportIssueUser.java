package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ReportIssueUser extends RoundedPanel{

    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JComboBox<String> typeOptionPane;
    private JButton tagsButton;
    private FileChooserPanel fileChooserPanel;
    protected JButton reportButton;
    protected JButton cancelButton;
    private static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String DESCRIPTION_PLACEHOLDER = "Inserisci descrizione";
    private static final String[] options = {"Bug", "Documentation", "Feature", "Question"};

    public ReportIssueUser(JFrame mainFrame, HomePanelUser homePanelUser) {

        super(new GridBagLayout());

        setRoundedPanel();
        setComponents(mainFrame, homePanelUser);

        setVisible(true);
    }

    protected void setComponents(JFrame mainFrame, HomePanelUser  homePanelUser) {

        setTitleTextField();
        setDescriptionTextArea();
        setTypeOptionPane();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setReportButton(homePanelUser);
        setCancelButton(homePanelUser);
    }

    private void setRoundedPanel() {

        setRoundBorderColor(ColorsList.EMPTY_COLOR);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    protected void setTitleTextField() {

        titleTextField = new JTextField(TITLE_PLACEHOLDER, 30);
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        setFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(titleTextField);

        Constraints.setConstraints(0, 1, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LAST_LINE_START,
                0.5f, 0.5f, new Insets(10, 10, 0, 10));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void setDescriptionTextArea() {

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

    protected void setTypeOptionPane() {

        typeOptionPane = new JComboBox<>(options);
        typeOptionPane.setBorder(BorderFactory.createEmptyBorder());
        typeOptionPane.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeOptionPane);

        Constraints.setConstraints(0, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        Constraints.setConstraints(1, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tagsButton, Constraints.getGridBagConstraints());
    }

    protected void setFileChooserPanel() {

        fileChooserPanel = new FileChooserPanel();

        Constraints.setConstraints(2, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(fileChooserPanel, Constraints.getGridBagConstraints());
    }

    protected void setReportButton(HomePanelUser homePanelUser) {

        reportButton = new JButton("Report");

        reportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                report(homePanelUser);
            }
        });

        Constraints.setConstraints(0, 4, 2, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(reportButton, Constraints.getGridBagConstraints());
    }

    protected void report(HomePanelUser homePanelUser) {

        homePanelUser.returnToDefaultContentPanel();
    }

    protected void setCancelButton(HomePanelUser homePanelUser) {

        cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(homePanelUser);
            }
        });

        Constraints.setConstraints(1, 4, 2, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(cancelButton, Constraints.getGridBagConstraints());
    }

    protected void cancel(HomePanelUser homePanelUser) {

        homePanelUser.returnToDefaultContentPanel();
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
