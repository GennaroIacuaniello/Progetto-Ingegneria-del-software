package frontend.gui;

import frontend.controller.IssueController;
import frontend.controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShowIssueAdmin extends ShowReportedIssueDeveloper {

    private RoundedPanel tmpPanel;
    private JTextField searchField;
    private IconButton searchButton;
    private static final String SEARCHFIELD_PLACEHOLDER = "Inserisci email developer per assegnare";

    public ShowIssueAdmin(JFrame parent) {

        super(parent);

        if (statusLabel.getText().equals("Stato: TODO")) {

            setTmpPanel();
            setSearchButton();
            setSearchField();
        }
    }

    private void setTmpPanel() {

        tmpPanel = new RoundedPanel(new GridBagLayout());

        tmpPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 5, 4, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(5, 5, 5, 5));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    private void setSearchButton() {

        searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 32, 32);

        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                showPopupMenu(search());
            }
        });

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_END, new Insets(5, 5, 5, 0));
        tmpPanel.add(searchButton, Constraints.getGridBagConstraints());
    }

    private List<String> search() {

        UserController.getInstance().searchDevOrAdminByEmailAndProject((searchField.getText().equals(SEARCHFIELD_PLACEHOLDER) ?
                "" : searchField.getText()));

        return UserController.getInstance().getUsersEmails();
    }

    private void showPopupMenu(List<String> developers) {

        JPopupMenu popupMenu = new JPopupMenu();

        for (String developer : developers) {

            JMenuItem item = new JMenuItem(developer);

            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    IssueController.getInstance().assignIssueToDeveloper(developer);
                    statusLabel.setText("Stato: ASSIGNED");
                    tmpPanel.setVisible(false);
                }
            });

            popupMenu.add(item);
        }

        popupMenu.show(searchButton, 0, searchButton.getHeight());
    }

    private void setSearchField() {

        searchField = new JTextField(SEARCHFIELD_PLACEHOLDER);

        searchField.setBackground(ColorsList.EMPTY_COLOR);
        searchField.setBorder(BorderFactory.createEmptyBorder());

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchField, SEARCHFIELD_PLACEHOLDER);

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, new Insets(5, 0, 5, 5));
        tmpPanel.add(searchField, Constraints.getGridBagConstraints());
    }
}
