package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TagsMenu extends MyDialog{

    private IconButton addButton;
    private final ArrayList<TagPanel> tagPanels = new ArrayList<>();
    private JScrollPane scrollPane;
    private JPanel tagsPanel;

    public TagsMenu(JFrame owner) {

        super(owner);

        setTagsPanel();
        setScrollPane();
        setAddButton();

        pack();
    }

    private void setTagsPanel() {

        tagsPanel = new JPanel(new GridBagLayout());

        tagsPanel.setBorder(BorderFactory.createEmptyBorder());
        tagsPanel.setBackground(ColorsList.EMPTY_COLOR);
    }

    private void setScrollPane() {

        scrollPane = new JScrollPane();

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        scrollPane.setViewportView(tagsPanel);

        scrollPane.setPreferredSize(new Dimension(300, 500));

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        contentPanel.add(scrollPane, Constraints.getGridBagConstraints());
    }

    private void setAddButton() {

        addButton = new IconButton("/frontend/gui/images/addButtonIcon.png", 30, 30);

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                addTagPanel();
            }
        });

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        buttonsPanel.add(addButton, Constraints.getGridBagConstraints());
    }

    private void addTagPanel() {

        tagPanels.add(new TagPanel(this, tagPanels.size()));

        Constraints.setConstraints(0, tagPanels.size() - 1, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        tagsPanel.add(tagPanels.getLast(), Constraints.getGridBagConstraints());

        tagsPanel.revalidate();
        tagsPanel.repaint();

        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JPanel getTagsPanel() {
        return tagsPanel;
    }

    public ArrayList<TagPanel> getTagPanels() {
        return tagPanels;
    }
}
