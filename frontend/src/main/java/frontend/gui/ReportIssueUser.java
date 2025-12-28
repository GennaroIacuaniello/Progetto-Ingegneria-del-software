package frontend.gui;

import frontend.controller.Controller;
import frontend.controller.ControllerTMP;
import frontend.controller.PriorityConverter;
import frontend.dto.IssueDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ReportIssueUser extends RoundedPanel{

    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JComboBox<String> typeComboBox;
    private TagsButton tagsButton;
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

        revalidate();
        repaint();
    }

    protected void setComponents(JFrame mainFrame, HomePanelUser  homePanelUser) {

        setTitleTextField();
        setDescriptionTextArea();
        setTypeComboBox();
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
        titleTextField.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleTextField.setHorizontalAlignment(SwingConstants.CENTER);
        titleTextField.setPreferredSize(new Dimension(300, 30));
        titleTextField.setMinimumSize(new Dimension(300, 30));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(titleTextField);

        Constraints.setConstraints(0, 1, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(10, 160, 10, 160));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void setDescriptionTextArea() {

        descriptionTextArea = new JTextArea(DESCRIPTION_PLACEHOLDER, 10, 60);
        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionTextArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(descriptionTextArea, DESCRIPTION_PLACEHOLDER);

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);
        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(ColorsList.EMPTY_COLOR);
        tmpScrollPane.setViewportView(descriptionTextArea);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 80, 10, 80));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void setTypeComboBox() {

        typeComboBox = new JComboBox<>(options);
        typeComboBox.setBorder(BorderFactory.createEmptyBorder());
        typeComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeComboBox);

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

        reportButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        reportButton.setBorder(BorderFactory.createEmptyBorder());
        reportButton.setBackground(ColorsList.EMPTY_COLOR);

        reportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                report(homePanelUser);
            }
        });

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(reportButton);

        tmpPanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
        tmpPanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(0, 4, 2, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void report(HomePanelUser homePanelUser) {

        IssueDTO issue = new IssueDTO();

        issue.setTitle((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()));
        issue.setDescription((descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER) ? "" : descriptionTextArea.getText()));
        issue.setType((String) Objects.requireNonNull(typeComboBox.getSelectedItem()));
        issue.setTags(tagsButton.getTags());
        issue.setPriority(PriorityConverter.stringToInt("Media"));

        ControllerTMP.reportIssue(issue);
        homePanelUser.returnToDefaultContentPanel();
    }

    protected void setCancelButton(HomePanelUser homePanelUser) {

        cancelButton = new JButton("Cancel");

        cancelButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.setBackground(ColorsList.EMPTY_COLOR);

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(homePanelUser);
            }
        });

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(cancelButton);

        tmpPanel.setRoundBorderColor(ColorsList.RED_BORDER_COLOR);
        tmpPanel.setBackground(ColorsList.RED_BACKGROUND_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(1, 4, 2, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected void cancel(HomePanelUser homePanelUser) {

        homePanelUser.returnToDefaultContentPanel();
    }
}
