package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ShowReportedIssueUser extends JDialog {

    protected RoundedPanel mainPanel;
    protected JLabel statusLabel;

    public ShowReportedIssueUser(JFrame parent) {

        super(parent, true);

        setDialog();
        setMainPanel(parent);

        setBackButton();
        setTitleLabel();
        setDescriptionTextArea();

        setReportingUserLabel();
        setReportDateLabel();
        setTagsList();
        setImageButton();

        setAssignedDeveloperLabel();
        setResolutionDateLabel();
        setTypeLabel();
        setStatusLabel();
    }

    private void setDialog() {

        setUndecorated(true);
        setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setMainPanel(JFrame parent) {

        mainPanel = new RoundedPanel(new GridBagLayout());

        mainPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        mainPanel.setBackground(ColorsList.BACKGROUND_COLOR);

        mainPanel.setMinimumSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));
        mainPanel.setPreferredSize(new Dimension((int)(parent.getWidth() * 0.75), (int)(parent.getHeight() * 0.75)));

        setContentPane(mainPanel);
    }

    protected void setBackButton() {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.png", 30, 30);

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, new Insets(5, 5, 5, 5));
        mainPanel.add(backButton, Constraints.getGridBagConstraints());
    }

    private void setTitleLabel() {

        JLabel titleLabel = new JLabel(IssueController.getInstance().getIssueTitle());
        titleLabel.setBorder(BorderFactory.createEmptyBorder());
        titleLabel.setBackground(ColorsList.EMPTY_COLOR);
        titleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 24));

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(titleLabel);

        Constraints.setConstraints(0, 1, 3, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.1f, 0.5f,
                new Insets(10, 60, 10, 0));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setDescriptionTextArea() {

        JTextArea descriptionTextArea = new JTextArea(IssueController.getInstance().getIssueDescription(), 8, 40);

        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionTextArea.setBackground(Color.WHITE);
        descriptionTextArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setFocusable(false);

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);

        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(ColorsList.EMPTY_COLOR);
        //tmpScrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);
        tmpScrollPane.setViewportView(descriptionTextArea);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 60, 10, 60));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setTypeLabel() {
        JLabel typeLabel = new JLabel("Tipo: " + IssueController.getInstance().getIssueType());

        typeLabel.setBorder(BorderFactory.createEmptyBorder());
        typeLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(typeLabel);

        Constraints.setConstraints(2, 4, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setTagsList() {

        IconButton tagsButton = new IconButton("/frontend/gui/images/tagsButton.png", 30, 30);

        JPopupMenu menu = new JPopupMenu();

        for (String tag : IssueController.getInstance().getIssueTagsAsList()) {

            JLabel tmpLabel = new JLabel(tag);
            tmpLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            menu.add(tmpLabel);
        }

        tagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.show(tagsButton, 0, tagsButton.getHeight());
            }
        });

        Constraints.setConstraints(2, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tagsButton, Constraints.getGridBagConstraints());
    }

    private void setImageButton() {

        IconButton imageButton = new IconButton("/frontend/gui/images/imageButton.png", 30, 30);

        imageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Desktop.getDesktop().open(IssueController.getInstance().getIssueImageAsFile());
                } catch (IOException ex) {
                    new FloatingMessage("Impossibile aprire il file.", imageButton, FloatingMessage.ERROR_MESSAGE);
                } catch (NullPointerException ex) {
                    new FloatingMessage("Nessuna immagine è stata allegata per questa issue", imageButton, FloatingMessage.WARNING_MESSAGE);
                }
            }
        });

        Constraints.setConstraints(3, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(imageButton, Constraints.getGridBagConstraints());
    }

    protected void setStatusLabel() {

        statusLabel = new JLabel("Stato: " + IssueController.getInstance().getIssueStatus());

        statusLabel.setBorder(BorderFactory.createEmptyBorder());
        statusLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(statusLabel);

        Constraints.setConstraints(3, 4, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setReportDateLabel() {

        JLabel reportDateLabel = new JLabel("Segnalazione: " + IssueController.getInstance().getIssueReportDate().toString());

        reportDateLabel.setBorder(BorderFactory.createEmptyBorder());
        reportDateLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(reportDateLabel);

        Constraints.setConstraints(1, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setResolutionDateLabel() {

        JLabel resolutionDateLabel = new JLabel("Risoluzione: " + (IssueController.getInstance().getIssueResolutionDate() != null ?
                IssueController.getInstance().getIssueResolutionDate().toString() : "questa issue non è ancora stata risolta"));

        resolutionDateLabel.setBorder(BorderFactory.createEmptyBorder());
        resolutionDateLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(resolutionDateLabel);

        Constraints.setConstraints(1, 4, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setReportingUserLabel() {

        JLabel reportingUserLabel = new JLabel("Segnalatore: " + IssueController.getInstance().getIssueReportingUser().getEmail());

        reportingUserLabel.setBorder(BorderFactory.createEmptyBorder());
        reportingUserLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(reportingUserLabel);

        Constraints.setConstraints(0, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    private void setAssignedDeveloperLabel() {

        JLabel assignedDeveloperLabel = new JLabel("Developer assegnato: " + ((IssueController.getInstance().getIssueAssignedDeveloper() != null) ?
                IssueController.getInstance().getIssueAssignedDeveloper().getEmail() : "questa issue non è ancora stata assegnata"));

        assignedDeveloperLabel.setBorder(BorderFactory.createEmptyBorder());
        assignedDeveloperLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(assignedDeveloperLabel);

        Constraints.setConstraints(0, 4, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }
}
