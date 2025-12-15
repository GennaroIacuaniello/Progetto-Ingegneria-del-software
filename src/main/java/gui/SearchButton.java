package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchButton extends IconButton {

    public SearchButton(HomePanel homePanel) {

        super("/gui/images/searchButton.png", 30, 30);

        setActionListener(homePanel);
    }

    private void setActionListener(HomePanel homePanel) {
        //todo: implementa
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                List<String> ids = new ArrayList<>();
                List<String> names = new ArrayList<>();

                ids.add("id");
                names.add("name");

                new SearchProjectResultsUser(homePanel, ids, names);
            }
        });
    }
}
