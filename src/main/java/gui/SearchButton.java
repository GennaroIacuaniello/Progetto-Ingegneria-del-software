package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchButton extends IconButton {

    public SearchButton(JFrame mainFrame, HomePanel homePanel) {

        super("/gui/images/searchButton.png", 30, 30);

        setActionListener(mainFrame, homePanel);
    }

    private void setActionListener(JFrame mainFrame, HomePanel homePanel) {
        //todo: implementa
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                List<String> ids = new ArrayList<>();
                List<String> names = new ArrayList<>();

                ids.add("id");
                names.add("name");

                new SearchProjectResultsUser(mainFrame, homePanel, ids, names);
            }
        });
    }
}
