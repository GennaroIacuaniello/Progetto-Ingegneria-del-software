package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    protected static HomePage instance;
    protected JFrame mainFrame;
    protected final Color bgColor = new Color(204, 229, 255);

    protected HomePage() {

        setMainFrame();
        setPanels();

        mainFrame.setVisible(true);
    }

    protected static void setFlatLaf() {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            String[] options = {"Continua", "Chiudi"};
            int action = JOptionPane.showOptionDialog(null, "<html><center>Il tuo device non supporta FlatLaf,<br>" +
                            "utilizzerai un'altra versione dell'app,<br>" +
                            "tutte le funzioni rimarranno invariate.</center></html>",
                    "Errore nel caricamento grafica", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
            if (action == 1 || action == JOptionPane.CLOSED_OPTION) {
                return;
            }
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

    protected void updateHomePanelConstraints() {

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

        setFlatLaf();

        HomePage homePage = HomePage.getInstance();
    }

    public Color getBackgroundColor() {
        return bgColor;
    }
}
