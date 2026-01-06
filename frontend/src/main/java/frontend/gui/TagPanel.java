package frontend.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class TagPanel extends RoundedPanel{

    private JTextField tagField;
    @Getter
    private int index;
    private static final String TAGFIELD_PLACEHOLDER = "Inserisci etichetta";

    public TagPanel(TagsMenu menu, int index) {

        super(new GridBagLayout());

        this.index = index;

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.white);

        setField();
        setRemoveTagButton(menu);
    }

    private void setField() {

        tagField = new JTextField(TAGFIELD_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(tagField, TAGFIELD_PLACEHOLDER);

        tagField.setBorder(BorderFactory.createEmptyBorder());
        tagField.setBackground(ColorsList.EMPTY_COLOR);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(tagField, Constraints.getGridBagConstraints());
    }

    private void setRemoveTagButton(TagsMenu menu) {

        RemoveTagButton removetagButton = new RemoveTagButton(menu, this);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
        add(removetagButton, Constraints.getGridBagConstraints());
    }

    public void decrement() {

        index--;
    }

    public String getTagField() {
        return tagField.getText();
    }
}
