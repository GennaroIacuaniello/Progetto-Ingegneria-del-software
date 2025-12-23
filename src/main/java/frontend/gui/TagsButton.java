package frontend.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TagsButton extends IconButton {

    private TagsMenu tagsMenu;

    public TagsButton(JFrame mainFrame) {

        super("/frontend/gui/images/tagsButton.png", 40, 40);

        setActionListener(mainFrame);
    }

    private void setActionListener(JFrame mainFrame) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (tagsMenu == null)
                    tagsMenu = new TagsMenu(mainFrame);

                tagsMenu.setLocationRelativeTo(mainFrame);
                tagsMenu.setVisible(true);
            }
        });
    }
}
