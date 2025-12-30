package frontend.gui;

import frontend.controller.ControllerTMP;

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
    private static final String YEARTEXTFIELD_PLACEHOLDER = "Inserisci anno";

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

        IconButton backButton = new IconButton("/frontend/gui/images/backIconButton.png", 30, 30);

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                searchPage.homePanelReturnToDefaultContentPane();
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.1f, 0.1f,
                new Insets(5, 5, 5, 5));
        this.add(backButton, Constraints.getGridBagConstraints());
    }

    private void setSearchButton() {

        searchButton = new IconButton("/frontend/gui/images/searchButton.png", 30, 30);

        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                search();
            }
        });

        Constraints.setConstraints(1, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(searchButton, Constraints.getGridBagConstraints());
    }

    private void search() {

        if(!yearTextField.getText().equals(YEARTEXTFIELD_PLACEHOLDER) &&
        yearTextField.getText().matches("\\d+")) {

            ControllerTMP.createReport(Objects.requireNonNull(monthComboBox.getSelectedItem()).toString(), yearTextField.getText());

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

        Constraints.setConstraints(2, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setYearTextField() {

        yearTextField = new JTextField(YEARTEXTFIELD_PLACEHOLDER);

        yearTextField.setBackground(ColorsList.EMPTY_COLOR);
        yearTextField.setBorder(BorderFactory.createEmptyBorder());

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(yearTextField, YEARTEXTFIELD_PLACEHOLDER);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(yearTextField);

        Constraints.setConstraints(3, 1, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }
}
