package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchButtonUser extends IconButton {

    public SearchButtonUser(JFrame mainFrame, HomePanelUser homePanel) {

        super("/gui/images/searchButton.png", 30, 30);

        setActionListener(mainFrame, homePanel);
    }

    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //todo: ricerca che ritorna lista di ids e nomi progetti
                List<String> ids = new ArrayList<>();
                List<String> names = new ArrayList<>();

                ids.add("id");
                names.add("name");

                new SearchProjectResultsUser(mainFrame, homePanel, ids, names);
            }
        });
    }
}
