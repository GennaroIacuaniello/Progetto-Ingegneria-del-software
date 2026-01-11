package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ReportedIssueSearchPanelUser extends RoundedPanel{

    protected SearchReportedIssuePageUser searchPage;
    private RoundedPanel titlePanel;
    protected JTextField titleTextField;
    protected JComboBox<String> statusComboBox;
    protected TagsButton tagsButton;
    protected JComboBox<String> typeComboBox;
    protected static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    protected static final String ALL_PLACEHOLDER = "Tutte";
    private static final String[] statusOptions = {ALL_PLACEHOLDER , "To do", "Assegnate", "Risolte"};
    private static final String[] typeOptions = {ALL_PLACEHOLDER, "Bug", "Documentazione", "Feature", "Domanda"};


    public ReportedIssueSearchPanelUser(JFrame mainFrame, SearchReportedIssuePageUser searchPage) {

        super(new GridBagLayout());

        this.searchPage = searchPage;

        setPanel();

        setBackButton(searchPage);
        setUpperPanel();
        setSearchButton(mainFrame);
        setTitleTextField(mainFrame);
        setStatusComboBox();
        setTagsButton(mainFrame);
        setTypeComboBox();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }

    private void setBackButton(SearchReportedIssuePageUser searchPage) {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(e -> searchPage.homePanelReturnToDefaultContentPane());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.01f, 0.01f,
                new Insets(5, 5, 5, 5));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    private void setUpperPanel() {

        titlePanel = new RoundedPanel(new GridBagLayout());

        titlePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        titlePanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 1, 7, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                1f, 0.5f);
        this.add(titlePanel, Constraints.getGridBagConstraints());
    }

    private void setSearchButton(JFrame mainFrame) {

        IconButton searchIssuesButton = new IconButton("/frontend/gui/images/searchButton.svg", 32, 32);

        searchIssuesButton.addActionListener(e -> searchButtonActionListener(mainFrame));

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        titlePanel.add(searchIssuesButton, Constraints.getGridBagConstraints());
    }

    protected void searchButtonActionListener(JFrame mainFrame) {

        boolean success = IssueController.getInstance().searchReportedIssues((titleTextField.getText().equals(TITLE_PLACEHOLDER) ? "" : titleTextField.getText()),
                formatIssueStatus(Objects.requireNonNull(statusComboBox.getSelectedItem())), tagsButton.getTags(), formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem())), null);

        if(!success)
            return;

        new ReportedIssueSearchResultsPanelUser(mainFrame, searchPage, IssueController.getInstance().getIssuesTitles());
    }

    private void setTitleTextField(JFrame mainFrame) {

        titleTextField = new JTextField(TITLE_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(titleTextField, TITLE_PLACEHOLDER);

        titleTextField.setPreferredSize(new Dimension(150, 20));
        titleTextField.setMinimumSize(new Dimension(150, 20));
        titleTextField.setBorder(BorderFactory.createEmptyBorder());

        titleTextField.addActionListener(e -> searchButtonActionListener(mainFrame));

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 0, 5, 5));
        titlePanel.add(titleTextField, Constraints.getGridBagConstraints());
    }

    private void setStatusComboBox() {

        statusComboBox = new JComboBox<>(statusOptions);

        statusComboBox.setBorder(BorderFactory.createEmptyBorder());
        statusComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(statusComboBox);

        Constraints.setConstraints(0, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Stato: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setTagsButton(JFrame mainFrame) {

        tagsButton = new TagsButton(mainFrame);

        Constraints.setConstraints(2, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
        this.add(tagsButton, Constraints.getGridBagConstraints());
    }

    private void setTypeComboBox() {

        typeComboBox = new JComboBox<>(typeOptions);

        typeComboBox.setBackground(ColorsList.EMPTY_COLOR);
        typeComboBox.setBorder(BorderFactory.createEmptyBorder());

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(typeComboBox);

        Constraints.setConstraints(3, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Tipo: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(4, 2, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    protected String formatIssueType(Object issueType) {

        return switch (issueType.toString()) {
            case "Bug" -> "BUG";
            case "Documentation" -> "DOCUMENTATION";
            case "Feature" -> "FEATURE";
            case "Domanda" -> "QUESTION";
            default -> null;
        };
    }

    protected String formatIssueStatus(Object issueStatus) {

        return switch (issueStatus.toString()) {
            case "Assegnate" -> "ASSIGNED";
            case "Risolte" -> "RESOLVED";
            case "To do" -> "TODO";
            default -> null;
        };
    }

    protected JLabel createTransparentLabel(String text) {

        JLabel label = new JLabel(text);

        label.setBackground(ColorsList.EMPTY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder());

        return label;
    }
}
