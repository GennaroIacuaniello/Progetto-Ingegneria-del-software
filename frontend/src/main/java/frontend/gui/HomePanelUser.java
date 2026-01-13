package frontend.gui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello principale della Home Page per l'Utente standard.
 * <p>
 * Questa classe definisce il layout base della dashboard post-login.
 * Le sue responsabilità principali includono:
 * <ul>
 * <li>Configurazione del contenitore principale arrotondato.</li>
 * <li>Posizionamento del pulsante di Logout.</li>
 * <li>Gestione dell'area centrale dinamica ({@code contentPanel}), che inizialmente mostra
 * la ricerca dei progetti, ma può essere sostituita per mostrare altre viste (es. dettagli di un progetto).</li>
 * <li>Fornitura di metodi per aggiornare i risultati della ricerca o tornare alla vista iniziale.</li>
 * </ul>
 * È progettata per essere estesa da {@link HomePanelDeveloper} e {@link HomePanelAdmin}.
 * </p>
 */
public class HomePanelUser {

    /**
     * Riferimento al pannello di contenuto predefinito (vista ricerca progetti).
     * Utile per ripristinare la vista iniziale quando si torna indietro.
     */
    protected RoundedPanel defaultContentPanel;

    /**
     * Il pannello attualmente visualizzato al centro della dashboard.
     */
    protected RoundedPanel contentPanel;

    /**
     * Il contenitore principale dell'intera dashboard.
     */
    @Getter
    protected RoundedPanel homePanel;

    /**
     * Componente che gestisce la visualizzazione scorrevole dei risultati di ricerca.
     */
    protected SearchViewResults searchViewResults;

    /**
     * Titolo predefinito della pagina.
     */
    private static final String TITLE_PLACEHOLDER = "PROGETTI";

    /**
     * Costruttore del pannello utente.
     * <p>
     * Inizializza il pannello principale, aggiunge il pulsante di logout e
     * imposta la vista predefinita (pannello di ricerca progetti).
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     */
    protected HomePanelUser(JFrame mainFrame) {

        setHomePanel();
        setLogOutButton(mainFrame);
        setDefaultContentPanel(mainFrame);
    }

    /**
     * Configura lo stile e il layout del pannello principale.
     */
    private void setHomePanel() {

        homePanel = new RoundedPanel(new GridBagLayout());

        homePanel.setBackground(ColorsList.BACKGROUND_COLOR);
        homePanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
    }

    /**
     * Crea e posiziona il pulsante di Logout nell'angolo in alto a destra.
     *
     * @param mainFrame Il frame principale, necessario per gestire la logica di logout (chiusura/cambio schermata).
     */
    private void setLogOutButton(JFrame  mainFrame) {

        LogOutButton logOutButton = new LogOutButton(mainFrame);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_END,
                new Insets(10, 0, 0, 10));
        homePanel.add(logOutButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pannello di contenuto predefinito (Ricerca Progetti).
     * <p>
     * Crea un nuovo pannello, vi inserisce i filtri di ricerca e l'area dei risultati,
     * e lo imposta come contenuto corrente. Aggiorna anche il titolo della pagina.
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    private void setDefaultContentPanel(JFrame mainFrame) {

        contentPanel = new RoundedPanel(new GridBagLayout());
        contentPanel.setRoundBorderColor(ColorsList.EMPTY_COLOR);
        contentPanel.setBackground(ColorsList.EMPTY_COLOR);

        setSearchProjectPanel(mainFrame);
        setSearchProjectViewResults();

        setContentPanel(contentPanel);
        defaultContentPanel = contentPanel;
        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);
    }

    /**
     * Inizializza e aggiunge il pannello dei filtri di ricerca progetti.
     * <p>
     * Questo metodo è {@code protected} per permettere alle sottoclassi (Developer/Admin)
     * di sovrascriverlo e iniettare una versione più avanzata del pannello di ricerca
     * (es. con tasto "Crea Progetto" o "Elimina").
     * </p>
     *
     * @param mainFrame Il frame principale.
     */
    protected void setSearchProjectPanel(JFrame  mainFrame) {

        SearchProjectPanelUser searchProjectPanel = new SearchProjectPanelUser(mainFrame, this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER, 0.1f, 0.25f);
        contentPanel.add(searchProjectPanel.getSearchProjectPanel(), Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza l'area scorrevole per i risultati della ricerca.
     */
    private void setSearchProjectViewResults() {

        searchViewResults = new SearchViewResults();

        Constraints.setConstraints(0, 1, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 0.1f, 1f);
        contentPanel.add(searchViewResults.getViewportScrollPane(), Constraints.getGridBagConstraints());
    }

    /**
     * Aggiorna il contenuto visualizzato nell'area dei risultati di ricerca.
     *
     * @param component Il nuovo componente (es. tabella o lista di card) da mostrare.
     */
    public void updateSearchProjectViewResults(Component component) {

        searchViewResults.updateViewportView(component);
    }

    /**
     * Sostituisce il pannello centrale corrente con uno nuovo.
     * <p>
     * Questo metodo è fondamentale per la navigazione interna alla dashboard.
     * Rimuove il vecchio pannello, aggiunge quello nuovo (con i vincoli corretti)
     * e forza il ridisegno dell'interfaccia.
     * </p>
     *
     * @param panel Il nuovo pannello da visualizzare al centro della dashboard.
     */
    public void setContentPanel(RoundedPanel panel) {

        homePanel.remove(contentPanel);

        contentPanel = panel;

        Constraints.setConstraints(0, 1, 2, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 1f, 1f,
                new Insets(10, 10, 10, 10));
        homePanel.add(contentPanel, Constraints.getGridBagConstraints());

        homePanel.revalidate();
        homePanel.repaint();
    }

    /**
     * Ripristina la visualizzazione iniziale (Lista Progetti).
     * <p>
     * Reimposta il {@code defaultContentPanel} come contenuto centrale e ripristina il titolo "PROGETTI".
     * </p>
     */
    public void returnToDefaultContentPanel() {

        setContentPanel(defaultContentPanel);
        TitlePanel.getInstance().setTitle(TITLE_PLACEHOLDER);
    }
}