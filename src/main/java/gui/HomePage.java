package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    private final JFrame mainFrame = new JFrame("HomePage");
    private final TitlePanel titlePanel = new TitlePanel();

    public HomePage() {

        setFlatLaf();

        setMainFrame();
        setPanels();

        mainFrame.setVisible(true);
    }

    private void setFlatLaf() {

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

        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setPanels() {

        Constraints.setConstraints(0, 0, 3, 1,
                GridBagConstraints.HORIZONTAL, 0, 50, GridBagConstraints.NORTH);
        mainFrame.add(titlePanel.getTitlePanel(), Constraints.getGridBagConstraints());
    }

    public static void main(String[] args) {

        new HomePage();
    }
}
