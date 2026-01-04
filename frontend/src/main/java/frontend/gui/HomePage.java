package frontend.gui;

import com.formdev.flatlaf.FlatLightLaf;
import frontend.controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    protected JFrame mainFrame;
    private HomePanelUser homePanel;

    public HomePage() {

        double scale = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .getDefaultTransform().getScaleX();
        System.out.println("Current Scaling: " + (scale * 100) + "%");

        setMainFrame();
        setPanels();

        mainFrame.setVisible(true);
    }

    public static void setFlatLaf() {

        System.setProperty("flatlaf.uiScale", "1.25");
        System.setProperty("flatlaf.useTextAntialiasing", "true");

        try {

            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.graphicsAntialiasing", true);

        } catch (Exception ex) {
            System.err.println("Errore nell'inizializzazione di FlatLaf: " + ex.getMessage());
        }
    }

    private void setMainFrame() {

        mainFrame = new JFrame("Home Page");

        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.getContentPane().setBackground(ColorsList.FRAME_COLOR);
    }

    private void setPanels() {

        setTitlePanel();
        setHomePanel();
    }

    private void setTitlePanel() {

        TitlePanel titlePanel = TitlePanel.getInstance();

        Constraints.setConstraints(0, 0, 3, 1,
                GridBagConstraints.BOTH, 0, 50,
                GridBagConstraints.NORTH, new Insets(10, 10, 10, 10));
        mainFrame.add(titlePanel.getTitlePanel(), Constraints.getGridBagConstraints());
    }

    private void setHomePanel() {

        switch (AuthController.getInstance().getLoggedUser().getRole()) {

            case 0:
                homePanel = new HomePanelUser(mainFrame);
                break;

            case 1:
                homePanel = new HomePanelDeveloper(mainFrame);
                break;

            case 2:
                homePanel = new HomePanelAdmin(mainFrame);
                break;
        }


        mainFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateHomePanelConstraints();
            }
        });
    }

    protected void updateHomePanelConstraints() {

        mainFrame.remove(homePanel.getHomePanel());

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0,
                GridBagConstraints.PAGE_START, 0.01f, 0.1f,
                new Insets(10, (int)(mainFrame.getWidth() * 0.1), 10, (int)(mainFrame.getWidth() * 0.1)));
        mainFrame.add(homePanel.getHomePanel(), Constraints.getGridBagConstraints());

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
}
