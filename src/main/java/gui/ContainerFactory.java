package gui;

import java.awt.*;

public class ContainerFactory {

    public static RoundedPanel createRoundedPanelContainer(Component component) {

        RoundedPanel tmpPanel = new RoundedPanel(new GridBagLayout());

        tmpPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(5, 5, 5, 5));
        tmpPanel.add(component, Constraints.getGridBagConstraints());

        return tmpPanel;
    }
}
