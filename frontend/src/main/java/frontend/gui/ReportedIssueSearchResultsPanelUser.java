package frontend.gui;

import java.awt.*;

public class ReportedIssueSearchResultsPanelUser extends RoundedPanel{

    public ReportedIssueSearchResultsPanelUser() {

        super(new GridBagLayout());

        setPanel();
    }

    private void setPanel() {

        this.setRoundBorderColor(ColorsList.BORDER_COLOR);
        this.setBackground(Color.WHITE);
    }
}
