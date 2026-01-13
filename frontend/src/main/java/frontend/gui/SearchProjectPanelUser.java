package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Gestore del pannello di ricerca progetti (Lato Utente Standard).
 * <p>
 * Questa classe costruisce la barra di ricerca visualizzata nella dashboard.
 * Graficamente, è composta da un pannello arrotondato contenente:
 * <ul>
 * <li>Una casella di testo per inserire il nome del progetto.</li>
 * <li>Un pulsante (lente d'ingrandimento) per avviare la ricerca.</li>
 * </ul>
 * Questa classe è progettata per essere estesa: mentre il campo di testo rimane uguale per tutti,
 * il pulsante di ricerca viene sostituito nelle sottoclassi (Developer/Admin) per attivare
 * logiche di ricerca differenti.
 * </p>
 */
public class SearchProjectPanelUser {

    /**
     * Il pannello grafico effettivo (contenitore arrotondato).
     */
    protected final RoundedPanel searchProjectPanel;

    /**
     * Il campo di testo per l'input dell'utente.
     */
    protected JTextField searchTextField;

    /**
     * Testo segnaposto per il campo di input.
     */
    protected final String TEXTFIELD_PLACEHOLDER = "Inserire nome progetto";

    /**
     * Costruttore del pannello di ricerca.
     * <p>
     * Inizializza il contenitore grafico {@link RoundedPanel}, ne imposta lo stile (sfondo bianco, bordo colorato)
     * e aggiunge i componenti interni (campo di testo e bottone).
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello Home (necessario al bottone per aggiornare la vista risultati).
     */
    public SearchProjectPanelUser(JFrame mainFrame, HomePanelUser homePanel) {

        searchProjectPanel = new RoundedPanel(new GridBagLayout());

        searchProjectPanel.setBackground(Color.WHITE);
        searchProjectPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);

        setSearchTextField();
        setSearchButton(mainFrame, homePanel);
    }

    /**
     * Configura il campo di testo per la ricerca.
     * <p>
     * Imposta le dimensioni, rimuove il bordo predefinito (per integrarsi nel design arrotondato)
     * e attiva il comportamento del placeholder (testo che scompare al focus).
     * </p>
     */
    private void setSearchTextField() {

        searchTextField = new JTextField(TEXTFIELD_PLACEHOLDER);

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, TEXTFIELD_PLACEHOLDER);

        searchTextField.setPreferredSize(new Dimension(150, 20));
        searchTextField.setMinimumSize(new Dimension(150, 20));
        searchTextField.setBorder(BorderFactory.createEmptyBorder());

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 10, 0, GridBagConstraints.CENTER,
                new Insets(5, 0, 5, 5));
        searchProjectPanel.add(searchTextField, Constraints.getGridBagConstraints());
    }

    /**
     * Configura e aggiunge il pulsante di ricerca.
     * <p>
     * Questo metodo è {@code protected} per consentire l'override da parte di {@link SearchProjectPanelDeveloper}
     * e {@link SearchProjectPanelAdmin}.
     * Nella versione base, istanzia {@link SearchProjectsButtonUser}, che esegue una ricerca standard
     * e mostra risultati con azioni limitate (solo "Segnala Issue" e "Issue Segnalate").
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello Home.
     */
    protected void setSearchButton(JFrame mainFrame, HomePanelUser homePanel) {

        SearchProjectsButtonUser searchButton = new SearchProjectsButtonUser(mainFrame, homePanel, searchTextField, TEXTFIELD_PLACEHOLDER);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 0));
        searchProjectPanel.add(searchButton, Constraints.getGridBagConstraints());
    }

    /**
     * Restituisce il pannello grafico costruito.
     * <p>
     * Poiché questa classe non estende JPanel, questo metodo è necessario per ottenere
     * il componente effettivo da aggiungere all'interfaccia utente.
     * </p>
     *
     * @return Il {@link JPanel} (specificamente un RoundedPanel) contenente la search bar.
     */
    public JPanel getSearchProjectPanel() {
        return searchProjectPanel;
    }
}