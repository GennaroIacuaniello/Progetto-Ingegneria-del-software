package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello della Home Page specifico per gli Amministratori.
 * <p>
 * Questa classe estende {@link HomePanelDeveloper}, ereditando tutte le funzionalità previste per
 * gli sviluppatori, ma arricchendo l'interfaccia con strumenti di gestione esclusivi.
 * In particolare, aggiunge un pulsante per il menu amministrativo (es. per la creazione utenti)
 * e utilizza una versione specializzata del pannello di ricerca progetti.
 * </p>
 */
public class HomePanelAdmin extends HomePanelDeveloper {

    /**
     * Costruttore del pannello Admin.
     * <p>
     * Inizializza la struttura base richiamando il costruttore della superclasse e
     * aggiunge il pulsante del menu amministrativo all'interfaccia.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     */
    protected HomePanelAdmin(JFrame mainFrame) {

        super(mainFrame);

        setMenuButton();
    }

    /**
     * Aggiunge il pulsante del menu amministrativo al pannello.
     * <p>
     * Crea un {@link MenuButton} e lo posiziona nell'angolo in alto a sinistra (First Line Start).
     * Questo pulsante solitamente apre un menu laterale o un dialogo per operazioni privilegiate
     * (come la creazione di nuovi account Admin o Developer).
     * </p>
     */
    private void setMenuButton() {

        MenuButton menuButton = new MenuButton();

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START,
                new Insets(10, 10, 0, 0));
        homePanel.add(menuButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pannello di ricerca progetti specifico per l'Amministratore.
     * <p>
     * Sovrascrive il metodo della superclasse per istanziare e inserire {@link SearchProjectPanelAdmin},
     * che offre funzionalità avanzate di gestione dei progetti (es. cancellazione) non disponibili
     * agli utenti standard o agli sviluppatori.
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    @Override
    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelAdmin(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }
}