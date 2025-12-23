package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.List;

public class IssueSearchPanelUser extends RoundedPanel{

    private RoundedPanel upperPanel;
    private JTextField titleTextField;
    private JComboBox<String> statusComboBox;
    private TagsButton tagsButton;
    private JComboBox<String> typeComboBox;
    private static final String TITLE_PLACEHOLDER = "Inserisci titolo";
    private static final String[] statusOptions = {"Tutte", "To do", "Assegnate", "Risolte"};
    private static final String[] typeOptions = {"Tutte", "Bug", "Documentazione", "Feature", "Domanda"};

    public IssueSearchPanelUser(JFrame mainFrame) {

        super(new GridBagLayout());

        setPanel();

        setUpperPanel();
        setSearchButton();
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

    private void setSearchButton() {

        IconButton searchIssuesButton = new IconButton("/frontend/gui/images/searchButton.png", 30, 30);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        upperPanel.add(searchIssuesButton, Constraints.getGridBagConstraints());
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
