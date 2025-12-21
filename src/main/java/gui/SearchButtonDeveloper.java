package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchButtonDeveloper extends SearchButtonUser{

    public SearchButtonDeveloper(JFrame mainFrame, HomePanelUser homePanel) {

        super(mainFrame, homePanel);
    }

    @Override
    protected void setActionListener(JFrame mainFrame, HomePanelUser homePanel) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //todo: ricerca che ritorna lista di ids e nomi progetti
                java.util.List<String> ids = new ArrayList<>();
                List<String> names = new ArrayList<>();

                ids.add("id");
                names.add("name");

                new SearchProjectResultsDeveloper(mainFrame, homePanel, ids, names);
            }
        });
    }
}
