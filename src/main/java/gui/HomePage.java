package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    private static HomePage instance;
    private JFrame mainFrame;
    private final Color bgColor = new Color(204, 229, 255);

    private HomePage() {

        //setFlatLaf();

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

        mainFrame.getContentPane().setBackground(bgColor);
    }

    private void setPanels() {

        setTitlePanel();
        setHomePanel();
    }

    private void setTitlePanel() {

        TitlePanel titlePanel = new TitlePanel();

        Constraints.setConstraints(0, 0, 3, 1,
                GridBagConstraints.BOTH, 0, 50,
                GridBagConstraints.NORTH, new Insets(10, 10, 10, 10));
        mainFrame.add(titlePanel.getTitlePanel(), Constraints.getGridBagConstraints());
    }

    private void setHomePanel() {

        mainFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateHomePanelConstraints();
            }
        });
    }

    private void updateHomePanelConstraints() {

        mainFrame.remove(HomePanel.getInstance().getHomePanel());

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0,
                GridBagConstraints.PAGE_START, 0.01f, 0.1f,
                new Insets(10, (int)(mainFrame.getWidth() * 0.1), 10, (int)(mainFrame.getWidth() * 0.1)));
        mainFrame.add(HomePanel.getInstance().getHomePanel(), Constraints.getGridBagConstraints());

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static HomePage getInstance() {

        if (instance == null)
            instance = new HomePage();

        return instance;
    }

    public static void main(String[] args) {

        HomePage homePage = HomePage.getInstance();
    }

    public Color getBackgroundColor() {
        return bgColor;
    }
}
