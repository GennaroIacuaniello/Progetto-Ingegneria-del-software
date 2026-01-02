package frontend.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveTagButton extends IconButton{

    public RemoveTagButton(TagsMenu menu, TagPanel tagPanel) {

        super("/frontend/gui/images/trashButtonIcon.png", 32 ,32);

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removePanel(menu, tagPanel);
            }
        });
    }

    private void removePanel(TagsMenu menu, TagPanel tagPanel) {

        //rimuovi panel
        menu.getTagsPanel().remove(tagPanel);

        //aggiusta arrayLists
        menu.getTagPanels().remove(tagPanel);

        //aggiusta indici e panels
        for (int i = tagPanel.getIndex(); i < menu.getTagPanels().size(); i++) {

            menu.getTagPanels().get(i).decrement();

            menu.getTagsPanel().remove(menu.getTagPanels().get(i));

            Constraints.setConstraints(0, i, 1, 1,
                    GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                    new Insets(5, 5, 5, 5));
            menu.getTagsPanel().add(menu.getTagPanels().get(i), Constraints.getGridBagConstraints());
        }

        menu.getTagsPanel().revalidate();
        menu.getTagsPanel().repaint();

        menu.getScrollPane().revalidate();
        menu.getScrollPane().repaint();
    }
}
