package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TagsButton extends IconButton {

    public TagsButton(JFrame mainFrame) {

        super("/gui/images/tagsButton.png", 30, 30);

        setActionListener(mainFrame);
    }

    private void setActionListener(JFrame mainFrame) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JDialog dialog = new TagsMenu(mainFrame);
                dialog.setVisible(true);
            }
        });
    }
}
