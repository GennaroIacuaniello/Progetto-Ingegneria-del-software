package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchProjectPanel {

    //todo:
    /*
       deve contenere la barra di ricerca con JTextField per inserire il nome del progetto e JButton per effettuare la ricerca.
       I risultati della ricerca devono essere inseriti in SearchProjectResults.
       Le componenti devono essere inserite in un JPanel che la classe HomePanel prenderà tramite un metodo e inserirà nel JPanel apposito
       Controllare se la classe può essere implementata tramite design pattern singleton
    */

    private RoundedPanel searchProjectPanel;
    private JButton searchButton;
    private JTextField searchTextField;
    private final String textFieldInitializer = "Inserire nome progetto";

    public SearchProjectPanel() {

        searchProjectPanel = new RoundedPanel(new FlowLayout(FlowLayout.LEFT));

        searchProjectPanel.setBackground(Color.WHITE);
        searchProjectPanel.setRoundBorderColor(new Color (77, 133, 255));

        setSearchTextField();
        setSearchButton();
    }

    private void setSearchTextField() {

        searchTextField = new JTextField(textFieldInitializer);

        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (searchTextField.getText().equals(textFieldInitializer)) {
                    searchTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText(textFieldInitializer);
                }
            }
        });

        searchTextField.setBorder(BorderFactory.createEmptyBorder());

        searchProjectPanel.add(searchTextField);
    }

    private void setSearchButton() {

        searchButton = new JButton("Cerca");

        searchProjectPanel.add(searchButton);
    }

    public JPanel getSearchProjectPanel() {
        return searchProjectPanel;
    }
}
