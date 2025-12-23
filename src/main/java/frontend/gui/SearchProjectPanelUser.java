package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchProjectPanelUser {

    //todo:
    /*
       deve contenere la barra di ricerca con JTextField per inserire il nome del progetto e JButton per effettuare la ricerca.
       I risultati della ricerca devono essere inseriti in SearchProjectResults.
       Le componenti devono essere inserite in un JPanel che la classe HomePanel prenderà tramite un metodo e inserirà nel JPanel apposito
       Controllare se la classe può essere implementata tramite design pattern singleton
    */

    protected final RoundedPanel searchProjectPanel;
    protected JTextField searchTextField;
    protected final String TEXTFIELD_PLACEHOLDER = "Inserire nome progetto";

    public SearchProjectPanelUser(JFrame mainFrame, HomePanelUser homePanel) {

        searchProjectPanel = new RoundedPanel(new GridBagLayout());

        searchProjectPanel.setBackground(Color.WHITE);
        searchProjectPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        setSearchTextField();
        setSearchButton(mainFrame, homePanel);
    }

    private void setSearchTextField() {

        searchTextField = new JTextField(TEXTFIELD_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, TEXTFIELD_PLACEHOLDER);

        searchTextField.setPreferredSize(new Dimension(150, 20));
        searchTextField.setMinimumSize(new Dimension(150, 20));
        searchTextField.setBorder(BorderFactory.createEmptyBorder());

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 0, 5, 5));
        searchProjectPanel.add(searchTextField, Constraints.getGridBagConstraints());
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        SearchButtonUser searchButton = new SearchButtonUser(mainFrame, homePanel, searchTextField, TEXTFIELD_PLACEHOLDER);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        searchProjectPanel.add(searchButton, Constraints.getGridBagConstraints());
    }

    public JPanel getSearchProjectPanel() {
        return searchProjectPanel;
    }
}
