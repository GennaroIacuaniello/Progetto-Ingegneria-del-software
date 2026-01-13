package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello della Home Page specifico per gli Sviluppatori (Developer).
 * <p>
 * Questa classe estende {@link HomePanelUser}, ereditandone la struttura di base e il layout.
 * La differenza principale risiede nell'override del metodo {@code setSearchProjectPanel},
 * che sostituisce il pannello di ricerca standard con {@link SearchProjectPanelDeveloper}.
 * Questo permette allo sviluppatore di accedere a funzionalità privilegiate, come la creazione
 * di nuovi progetti, direttamente dalla dashboard.
 * </p>
 */
public class HomePanelDeveloper extends HomePanelUser{

    /**
     * Costruttore del pannello Developer.
     * <p>
     * Inizializza l'interfaccia richiamando il costruttore della superclasse {@link HomePanelUser},
     * che si occupa di impostare il layout generale.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     */
    public HomePanelDeveloper(JFrame mainFrame) {

        super(mainFrame);
    }

    /**
     * Configura il pannello di ricerca progetti specifico per lo Sviluppatore.
     * <p>
     * Sovrascrive il metodo della superclasse per istanziare e inserire {@link SearchProjectPanelDeveloper}
     * al posto di {@link SearchProjectPanelUser}.
     * </p>
     *
     * @param mainFrame Il frame principale, passato al pannello di ricerca per gestire eventuali dialoghi.
     */
    @Override
    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelDeveloper(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }
}