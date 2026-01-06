package frontend.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class TagsMenu extends MyDialog{

    private final ArrayList<TagPanel> tagPanels = new ArrayList<>();
    private JScrollPane scrollPane;
    private JPanel tagsPanel;

    public TagsMenu(JFrame owner) {

        super(owner);

        mainPanel.setMinimumSize(new Dimension(200, 400));
        mainPanel.setPreferredSize(new Dimension(200, 400));

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

        scrollPane.setPreferredSize(new Dimension(200, 350));

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.PAGE_START,
                1f, 1f, new Insets(5, 5, 5, 5));
        mainPanel.add(scrollPane, Constraints.getGridBagConstraints());
    }

    private void setAddButton() {

        IconButton addButton = new IconButton("/frontend/gui/images/addButtonIcon.svg", 32, 32);

        addButton.addActionListener(e -> addTagPanel());

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(5, 5, 5, 5));
        mainPanel.add(addButton, Constraints.getGridBagConstraints());
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

    @Override
    protected void backActionListener() {

        setVisible(false);
    }
}
