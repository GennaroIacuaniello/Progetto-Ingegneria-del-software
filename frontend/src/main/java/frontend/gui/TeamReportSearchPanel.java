package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TeamReportSearchPanel extends RoundedPanel{

    TeamReportPage searchPage;
    private IconButton searchButton;
    private JComboBox<String> monthComboBox;
    private JTextField yearTextField;
    private static final String YEAR_TEXTFIELD_PLACEHOLDER = "Inserisci anno";

    public TeamReportSearchPanel(TeamReportPage searchPage) {

        super(new GridBagLayout());

        this.searchPage = searchPage;

        setPanel();
        setBackButton();
        setSearchButton();
        setMonthComboBox();
        setYearTextField();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }

    private void setBackButton() {

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.svg", 32, 32);

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                searchPage.returnToManageTeamsPanel();
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.FIRST_LINE_START, 0.1f, 0.1f,
                new Insets(10, 10, 0, 0));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    private void setSearchButton() {

        searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 64, 64);

        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                search();
            }
        });

        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(searchButton, Constraints.getGridBagConstraints());
    }

    private void search() {

        if(!yearTextField.getText().equals(YEAR_TEXTFIELD_PLACEHOLDER) &&
        yearTextField.getText().matches("\\d+")) {

            TeamController.getInstance().createReport(Objects.requireNonNull(monthComboBox.getSelectedItem()).toString(), yearTextField.getText());

            new ReportResults(searchPage);
        } else
            new FloatingMessage("L'anno inserito non è valido", searchButton, JOptionPane.ERROR_MESSAGE);
    }

    private void setMonthComboBox() {

        monthComboBox = new JComboBox<>();

        monthComboBox.setBackground(ColorsList.EMPTY_COLOR);
        monthComboBox.setBorder(BorderFactory.createEmptyBorder());

        monthComboBox.addItem("Gennaio");
        monthComboBox.addItem("Febbraio");
        monthComboBox.addItem("Marzo");
        monthComboBox.addItem("Aprile");
        monthComboBox.addItem("Maggio");
        monthComboBox.addItem("Giugno");
        monthComboBox.addItem("Luglio");
        monthComboBox.addItem("Agosto");
        monthComboBox.addItem("Settembre");
        monthComboBox.addItem("Ottobre");
        monthComboBox.addItem("Novembre");
        monthComboBox.addItem("Dicembre");

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(monthComboBox);

        Constraints.setConstraints(1, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setYearTextField() {

        yearTextField = new JTextField(YEAR_TEXTFIELD_PLACEHOLDER);

        yearTextField.setBackground(ColorsList.EMPTY_COLOR);
        yearTextField.setBorder(BorderFactory.createEmptyBorder());

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(yearTextField, YEAR_TEXTFIELD_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(yearTextField);

        Constraints.setConstraints(2, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}
