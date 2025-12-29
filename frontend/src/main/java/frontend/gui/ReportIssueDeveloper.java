package frontend.gui;

import frontend.controller.IssueController;
import frontend.dto.IssueDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        setTitleTextField();
        setDescriptionTextArea();
        setTypeComboBox();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setPriorityComboBox();
        setReportButton(homePanelUser);
        setCancelButton(homePanelUser);
    }

    @Override
    protected void setReportButton(HomePanelUser homePanelUser) {

        reportButton = new JButton("Report");

        reportButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        reportButton.setBorder(BorderFactory.createEmptyBorder());
        reportButton.setBackground(ColorsList.EMPTY_COLOR);

        reportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (titleTextField.getText().equals(TITLE_PLACEHOLDER))
                    new FloatingMessage("Inserire il titolo per segnalare una issue", reportButton, FloatingMessage.ERROR_MESSAGE);
                else
                    report(homePanelUser);
            }
        });

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(reportButton);

        tmpPanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
        tmpPanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(0, 4, 3, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    @Override
    protected void report(HomePanelUser homePanelUser) {

        IssueDTO issue = new IssueDTO();

        issue.setTitle(titleTextField.getText());
        issue.setDescription((descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER) ? "" : descriptionTextArea.getText()));
        issue.setTypeWithString((String) Objects.requireNonNull(typeComboBox.getSelectedItem()));

        issue.setPriority(IssueController.getInstance().priorityStringToInt(Objects.requireNonNull(priorityComboBox.getSelectedItem()).toString()));

        IssueController.getInstance().reportIssue(issue, tagsButton.getTags(), fileChooserPanel.getSelectedFile());

        homePanelUser.returnToDefaultContentPanel();
    }

    @Override
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

        Constraints.setConstraints(1, 4, 3, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(options);
        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(3, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}
