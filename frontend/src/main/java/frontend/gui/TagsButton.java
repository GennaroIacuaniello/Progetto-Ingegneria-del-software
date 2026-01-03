package frontend.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TagsButton extends IconButton {

    private TagsMenu tagsMenu;

    public TagsButton(JFrame mainFrame) {

        super("/frontend/gui/images/tagsButton.svg", 40, 40);

        tagsMenu = new TagsMenu(mainFrame);
        tagsMenu.setLocationRelativeTo(mainFrame);
        tagsMenu.setVisible(false);

        setActionListener(mainFrame);
    }

    private void setActionListener(JFrame mainFrame) {

        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                tagsMenu.setLocationRelativeTo(mainFrame);
                tagsMenu.setVisible(true);
            }
        });
    }

    public List<String> getTags() {

        List<TagPanel> tagPanels = tagsMenu.getTagPanels();
        List<String> tags = new ArrayList<>();

        for (TagPanel tagPanel : tagPanels) {
            tags.add(tagPanel.getTagField());
        }

        return tags;
    }
}
