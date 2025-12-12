package gui;

import java.awt.*;

public class HomePageAdmin extends HomePage{

    protected HomePageAdmin() {

        super();
    }

    @Override
    protected void updateHomePanelConstraints() {

        mainFrame.remove(HomePanelAdmin.getInstance().getHomePanel());

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0,
                GridBagConstraints.PAGE_START, 0.01f, 0.1f,
                new Insets(10, (int)(mainFrame.getWidth() * 0.1), 10, (int)(mainFrame.getWidth() * 0.1)));
        mainFrame.add(HomePanelAdmin.getInstance().getHomePanel(), Constraints.getGridBagConstraints());

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static HomePage getInstance() {

        if (instance == null)
            instance = new HomePageAdmin();

        return instance;
    }

    public static void main(String[] args) {

        setFlatLaf();

        HomePage homePage = HomePageAdmin.getInstance();
    }
}
