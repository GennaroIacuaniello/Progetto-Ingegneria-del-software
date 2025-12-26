package frontend.gui;

import frontend.controller.Controller;
import frontend.controller.ControllerTMP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.List;

public class ReportedIssueSearchPanelUser extends RoundedPanel{

    protected SearchReportedIssuePageUser searchPage;
    private RoundedPanel upperPanel;
    protected JTextField titleTextField;
    protected JComboBox<String> statusComboBox;
    protected TagsButton tagsButton;
    protected JComboBox<String> typeComboBox;
    protected static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String[] statusOptions = {"Tutte", "To do", "Assegnate", "Risolte"};
    private static final String[] typeOptions = {"Tutte", "Bug", "Documentazione", "Feature", "Domanda"};

    public ReportedIssueSearchPanelUser(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(new GridBagLayout());

        this.searchPage = searchPage;

        setPanel();

        setUpperPanel();
        setSearchButton(mainFrame);
        setTitleTextField();
        setStatusComboBox();
        setTagsButton(mainFrame);
        setTypeComboBox();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }

    private void setUpperPanel() {

        upperPanel = new RoundedPanel(new GridBagLayout());

        upperPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        upperPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 4, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                1f, 0.5f);
        this.add(upperPanel, Constraints.getGridBagConstraints());
    }

    private void setSearchButton(JFrame mainFrame) {

        IconButton searchIssuesButton = new IconButton("/frontend/gui/images/searchButton.png", 30, 30);

        searchIssuesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonActionListener(mainFrame);
            }
        });

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        upperPanel.add(searchIssuesButton, Constraints.getGridBagConstraints());
    }

    protected void searchButtonActionListener(JFrame mainFrame) {

        ControllerTMP.searchReportedIssues(titleTextField.getText(), TITLE_PLACEHOLDER, (String)statusComboBox.getSelectedItem(),
                tagsButton.getTags(), (String)typeComboBox.getSelectedItem(), null);

        new ReportedIssueSearchResultsPanelUser(mainFrame, searchPage, ControllerTMP.getIssuesTitles());
    }

    private void setTitleTextField() {

        titleTextField = new JTextField(TITLE_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        titleTextField.setPreferredSize(new Dimension(150, 20));
        titleTextField.setMinimumSize(new Dimension(150, 20));
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 0, 5, 5));
        upperPanel.add(titleTextField, Constraints.getGridBagConstraints());
    }

    private void setStatusComboBox() {

        statusComboBox = new JComboBox<>(statusOptions);

        statusComboBox.setBorder(BorderFactory.createEmptyBorder());
        statusComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(statusComboBox);

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        Constraints.setConstraints(1, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tagsButton, Constraints.getGridBagConstraints());
    }

    private void setTypeComboBox() {

        typeComboBox = new JComboBox<>(typeOptions);

        typeComboBox.setBorder(BorderFactory.createEmptyBorder());
        typeComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeComboBox);

        Constraints.setConstraints(2, 1, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    public String getTitleTextFieldText() {
        return titleTextField.getText();
    }

    public String getStatusComboBoxText() {
        return Objects.requireNonNull(statusComboBox.getSelectedItem()).toString();
    }

    public List<String> getTagsButtonText() {
        return tagsButton.getTags();
    }

    public String getTypeComboBoxText() {
        return Objects.requireNonNull(typeComboBox.getSelectedItem()).toString();
    }
}
