package frontend.gui;

import com.formdev.flatlaf.FlatLightLaf;
import frontend.controller.AuthController;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principale che gestisce la finestra Home dell'applicazione.
 * <p>
 * Questa classe funge da container principale (Main Frame) dopo l'accesso.
 * Le sue responsabilità includono:
 * </p>
 * <ul>
 * <li>Inizializzazione del Look and Feel (FlatLaf).</li>
 * <li>Configurazione della finestra principale (`JFrame`).</li>
 * <li>Selezione dinamica del pannello di contenuto (Dashboard) in base al ruolo dell'utente loggato (Utente base, Sviluppatore, Admin).</li>
 * <li>Gestione del layout responsive tramite listener sul ridimensionamento della finestra.</li>
 * </ul>
 */
public class HomePage {

    /**
     * Il frame principale dell'applicazione.
     */
    @Getter
    protected JFrame mainFrame;

    /**
     * Il pannello centrale che contiene le funzionalità specifiche per il ruolo dell'utente.
     */
    private HomePanelUser homePanel;

    /**
     * Costruttore della HomePage.
     * <p>
     * Configura il frame, aggiunge i pannelli (Titolo e Dashboard) e rende la finestra visibile.
     * </p>
     */
    public HomePage() {

        setMainFrame();
        setPanels();

        mainFrame.setVisible(true);
    }

    /**
     * Configura il Look and Feel globale dell'applicazione utilizzando FlatLaf.
     * <p>
     * Imposta il tema "FlatLightLaf", definisce un fattore di scala per l'interfaccia (1.25x)
     * e abilita l'antialiasing per i testi e la grafica per migliorare la resa visiva.
     * </p>
     */
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

    /**
     * Inizializza le proprietà del frame principale.
     * <p>
     * Imposta titolo, layout (GridBagLayout), dimensioni, operazione di chiusura e colore di sfondo.
     * </p>
     */
    private void setMainFrame() {

        mainFrame = new JFrame("Home Page");

        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.getContentPane().setBackground(ColorsList.FRAME_COLOR);
    }

    /**
     * Orchestra l'aggiunta dei pannelli principali al frame.
     */
    private void setPanels() {

        setTitlePanel();
        setHomePanel();
    }

    /**
     * Aggiunge il pannello del titolo (Header) nella parte superiore del frame.
     */
    private void setTitlePanel() {

        TitlePanel titlePanel = TitlePanel.getInstance();

        Constraints.setConstraints(0, 0, 3, 1,
                GridBagConstraints.BOTH, 0, 50,
                GridBagConstraints.NORTH, new Insets(10, 10, 10, 10));
        mainFrame.add(titlePanel.getTitlePanel(), Constraints.getGridBagConstraints());
    }

    /**
     * Determina e istanzia il pannello home specifico in base al ruolo dell'utente.
     * <p>
     * Interroga l'{@link AuthController} per ottenere il ruolo dell'utente loggato:
     * <ul>
     * <li><b>0:</b> Utente standard (HomePanelUser)</li>
     * <li><b>1:</b> Sviluppatore (HomePanelDeveloper)</li>
     * <li><b>2:</b> Amministratore (HomePanelAdmin)</li>
     * </ul>
     * Aggiunge inoltre un {@link java.awt.event.ComponentAdapter} per aggiornare i vincoli
     * di layout quando la finestra viene ridimensionata.
     * </p>
     */
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

    /**
     * Aggiorna dinamicamente i vincoli di layout del pannello home.
     * <p>
     * Questo metodo viene chiamato al ridimensionamento della finestra per calcolare
     * margini laterali proporzionali (10% della larghezza del frame), garantendo
     * che il contenuto rimanga centrato e visivamente gradevole ("responsive").
     * </p>
     */
    protected void updateHomePanelConstraints() {

        // Rimuove temporaneamente il pannello per riaggiungerlo con i nuovi vincoli
        mainFrame.remove(homePanel.getHomePanel());

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0,
                GridBagConstraints.PAGE_START, 0.01f, 0.1f,
                new Insets(10, (int)(mainFrame.getWidth() * 0.1), 10, (int)(mainFrame.getWidth() * 0.1)));
        mainFrame.add(homePanel.getHomePanel(), Constraints.getGridBagConstraints());

        mainFrame.revalidate();
        mainFrame.repaint();
    }
}