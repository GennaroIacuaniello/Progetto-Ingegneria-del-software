package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class IssueSearchResultsPanel extends RoundedPanel{

    public IssueSearchResultsPanel() {

        super(new GridBagLayout());

        setPanel();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }
}
