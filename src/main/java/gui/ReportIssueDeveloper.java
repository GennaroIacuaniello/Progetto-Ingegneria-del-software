package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportIssueDeveloper extends ReportIssueUser {

    private JComboBox<String> priorityComboBox;
    private static final String[] options = {"Molto bassa", "Bassa", "Normale", "Alta", "Molto alta"};

    public ReportIssueDeveloper(JFrame mainFrame, HomePanelUser homePanelUser) {

        super(mainFrame,  homePanelUser);

        revalidate();
        repaint();
    }

    @Override
    protected void setComponents(JFrame mainFrame, HomePanelUser homePanelUser) {

        setTitleTextField();
        setDescriptionTextArea();
        setTypeOptionPane();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setPriorityComboBox();
        setReportButton(homePanelUser);
        setCancelButton(homePanelUser);
    }

    @Override
    protected void setReportButton(HomePanelUser homePanelUser) {

        reportButton = new JButton("Report");

        reportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                report(homePanelUser);
            }
        });

        Constraints.setConstraints(0, 4, 3, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(reportButton, Constraints.getGridBagConstraints());
    }

    @Override
    protected void setCancelButton(HomePanelUser homePanelUser) {

        cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(homePanelUser);
            }
        });

        Constraints.setConstraints(1, 4, 3, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(cancelButton, Constraints.getGridBagConstraints());
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
