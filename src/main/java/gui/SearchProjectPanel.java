package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchProjectPanel {

    //todo:
    /*
       deve contenere la barra di ricerca con JTextField per inserire il nome del progetto e JButton per effettuare la ricerca.
       I risultati della ricerca devono essere inseriti in SearchProjectResults.
       Le componenti devono essere inserite in un JPanel che la classe HomePanel prenderà tramite un metodo e inserirà nel JPanel apposito
       Controllare se la classe può essere implementata tramite design pattern singleton
    */

    private final RoundedPanel searchProjectPanel;
    private JTextField searchTextField;
    private final String TextFieldInitializer = "Inserire nome progetto";
    private final Color BorderColor = new Color (77, 133, 255);

    public SearchProjectPanel(HomePanel homePanel) {

        searchProjectPanel = new RoundedPanel(new FlowLayout(FlowLayout.LEFT));

        searchProjectPanel.setBackground(Color.WHITE);
        searchProjectPanel.setRoundBorderColor(BorderColor);

        setSearchButton(homePanel);
        setSearchTextField();
    }

    private void setSearchTextField() {

        searchTextField = new JTextField(TextFieldInitializer);

        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (searchTextField.getText().equals(TextFieldInitializer)) {
                    searchTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText(TextFieldInitializer);
                }
            }
        });

        searchTextField.setPreferredSize(new Dimension(150, 20));
        searchTextField.setBorder(BorderFactory.createEmptyBorder());

        searchProjectPanel.add(searchTextField);
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    private void setSearchButton(HomePanel homePanel) {

        SearchButton searchButton = new SearchButton(homePanel);

        searchProjectPanel.add(searchButton);
    }

    public JPanel getSearchProjectPanel() {
        return searchProjectPanel;
    }
}
