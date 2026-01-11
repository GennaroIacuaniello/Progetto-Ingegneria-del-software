package frontend.gui;

import frontend.controller.IssueController;
import frontend.dto.IssueDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ReportIssueDeveloper extends ReportIssueUser {

    private JComboBox<String> priorityComboBox;
    private static final String[] options = {"Molto bassa", "Bassa", "Media", "Alta", "Molto alta"};

    public ReportIssueDeveloper(JFrame mainFrame, HomePanelUser homePanelUser) {

        super(mainFrame,  homePanelUser);

        revalidate();
        repaint();
    }

    @Override
    protected void setComponents(JFrame mainFrame, HomePanelUser homePanelUser) {

        setBackButton(homePanelUser);
        setTitleTextField();
        setDescriptionTextArea();
        setTypeComboBox();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setPriorityComboBox();
        setReportButton(homePanelUser);
    }

    @Override
    protected void report(HomePanelUser homePanelUser) {

        IssueDTO issue = new IssueDTO();

        issue.setTitle(titleTextField.getText());
        issue.setDescription((descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER) ? "" : descriptionTextArea.getText()));
        issue.setTypeWithString((String) Objects.requireNonNull(typeComboBox.getSelectedItem()));

        issue.setPriority(IssueController.getInstance().priorityStringToInt(Objects.requireNonNull(priorityComboBox.getSelectedItem()).toString()));

        boolean success = IssueController.getInstance().reportIssue(issue, tagsButton.getTags(), fileChooserPanel.getSelectedFile());

        if(!success)
            return;


        homePanelUser.returnToDefaultContentPanel();
    }

    private void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(options);
        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(2, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Priorità: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(3, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    @Override
    protected void setTagsButtonConstraints() {

        Constraints.setConstraints(1, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }

    @Override
    protected void setFileChooserPanelConstraints() {

        Constraints.setConstraints(2, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }
}
