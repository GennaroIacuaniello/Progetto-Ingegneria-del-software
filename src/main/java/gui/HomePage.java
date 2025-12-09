package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    private static HomePage instance;
    private JFrame mainFrame;
    private HomePanel homePanel;

    private HomePage() {

        setFlatLaf();

        setMainFrame();
        setPanels();

        mainFrame.setVisible(true);
    }

    private void setFlatLaf() {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            //todo
        }
    }

    private void setMainFrame() {

        mainFrame = new JFrame("Home Page");

        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setPanels() {

        setTitlePanel();
        setHomePanel();
    }

    private void setTitlePanel() {

        TitlePanel titlePanel = new TitlePanel();

        Constraints.setConstraints(0, 0, 3, 1,
                GridBagConstraints.BOTH, 0, 50, GridBagConstraints.NORTH);
        mainFrame.add(titlePanel.getTitlePanel(), Constraints.getGridBagConstraints());
    }

    private void setHomePanel() {

        JPanel placeHolderPanelLeft = new JPanel();
        JPanel placeHolderPanelRight = new JPanel();

        placeHolderPanelLeft.setBackground(new Color(230, 255, 255));
        placeHolderPanelRight.setBackground(new Color(230, 255, 255));

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 30, 0, GridBagConstraints.PAGE_START, 0.01f, 0.1f);
        mainFrame.add(placeHolderPanelLeft, Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.PAGE_START, 0.05f, 0.1f);
        mainFrame.add(HomePanel.getInstance().getHomePanel(), Constraints.getGridBagConstraints());

        Constraints.setConstraints(2, 1, 1, 1,
                GridBagConstraints.BOTH, 30, 0, GridBagConstraints.PAGE_START, 0.01f, 0.1f);
        mainFrame.add(placeHolderPanelRight, Constraints.getGridBagConstraints());
    }

    public static HomePage getInstance() {

        if (instance == null)
            instance = new HomePage();

        return instance;
    }

    public static void main(String[] args) {

        HomePage homePage = HomePage.getInstance();
    }
}
