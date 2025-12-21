package gui;

import javax.swing.*;
import java.awt.*;

public class TagPanel extends RoundedPanel{

    JTextField tagField;
    RemoveTagButton removetagButton;
    private int index;

    public TagPanel(TagsMenu menu, int index) {

        super(new GridBagLayout());

        this.index = index;

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.white);

        setField();
        setRemoveTagButton(menu);
    }

    private void setField() {

        tagField = new JTextField();

        tagField.setBorder(BorderFactory.createEmptyBorder());
        tagField.setBackground(ColorsList.EMPTY_COLOR);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(tagField, Constraints.getGridBagConstraints());
    }

    private void setRemoveTagButton(TagsMenu menu) {

        removetagButton = new RemoveTagButton(menu, this);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(removetagButton, Constraints.getGridBagConstraints());
    }

    public int getIndex() {
        return index;
    }

    public void decrement() {

        index--;
    }
}
