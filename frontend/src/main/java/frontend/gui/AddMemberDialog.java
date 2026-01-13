package frontend.gui;

import frontend.controller.UserController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Finestra di dialogo per la ricerca e l'aggiunta di nuovi membri.
 * <p>
 * Questa classe estende {@link JDialog} e fornisce un'interfaccia grafica per cercare utenti
 * nel sistema tramite il loro indirizzo email. I risultati della ricerca vengono mostrati
 * in una tabella, dove ogni riga presenta un pulsante "Aggiungi" per selezionare l'utente desiderato.
 * </p>
 */
public class AddMemberDialog extends JDialog {

    /**
     * Campo di testo per l'inserimento dell'email da cercare.
     */
    private JTextField searchTextField;

    /**
     * Pannello destinato a contenere la tabella dei risultati della ricerca.
     */
    private final JPanel resultsPanel;

    /**
     * Testo segnaposto mostrato nella barra di ricerca quando è vuota.
     */
    private final String PLACEHOLDER = "Cerca email utente...";

    /**
     * Riferimento al frame principale dell'applicazione, utilizzato per gestire la gerarchia delle finestre.
     */
    private final JFrame mainFrame;

    /**
     * Costruttore della finestra di dialogo.
     * <p>
     * Inizializza il layout principale, la sezione di ricerca e il pannello dei risultati.
     * Esegue immediatamente una ricerca iniziale (spesso per mostrare tutti o una lista vuota) all'apertura.
     * </p>
     *
     * @param owner Il frame proprietario di questo dialogo (per renderlo modale rispetto ad esso).
     */
    public AddMemberDialog(JFrame owner) {
        super(owner, "Aggiungi Nuovo Membro", true);
        this.mainFrame = owner;

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        setupSearchSection(mainPanel);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
        // Configura i vincoli per far espandere il pannello dei risultati
        Constraints.setConstraints(0, 1, 1, 1, GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER, 1.0f, 1.0f, new Insets(20, 0, 0, 0));
        mainPanel.add(resultsPanel, Constraints.getGridBagConstraints());

        this.setContentPane(mainPanel);
        this.setSize(600, 450);
        this.setLocationRelativeTo(owner);

        performSearch();
    }

    /**
     * Configura e aggiunge la barra di ricerca al pannello principale.
     * <p>
     * Crea un pannello con bordi arrotondati ({@link RoundedPanel}) contenente il campo di testo
     * e il pulsante con l'icona della lente d'ingrandimento. Gestisce anche il comportamento
     * del focus per il testo segnaposto.
     * </p>
     *
     * @param mainPanel Il pannello principale a cui aggiungere la sezione di ricerca.
     */
    private void setupSearchSection(JPanel mainPanel) {

        RoundedPanel searchWrapper = new RoundedPanel(new GridBagLayout());

        Dimension wrapperDim = new Dimension(350, 50);
        searchWrapper.setPreferredSize(wrapperDim);
        searchWrapper.setMinimumSize(wrapperDim);
        searchWrapper.setMaximumSize(wrapperDim);

        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setRoundBorderColor(ColorsList.BORDER_COLOR);
        searchWrapper.setBorder(new EmptyBorder(5, 5, 5, 5));

        searchTextField = new JTextField(PLACEHOLDER);
        Dimension textDim = new Dimension(250, 30);
        searchTextField.setPreferredSize(textDim);
        searchTextField.setMinimumSize(textDim);
        searchTextField.setMaximumSize(textDim);

        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        TextComponentFocusBehaviour.setTextComponentFocusBehaviour(searchTextField, PLACEHOLDER);

        IconButton searchButton = new IconButton("/frontend/gui/images/searchButton.svg", 25, 25);
        searchButton.addActionListener(e -> performSearch());

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.0f, 0.0f, new Insets(0, 5, 0, 0));
        searchWrapper.add(searchButton, Constraints.getGridBagConstraints());

        Constraints.setConstraints(1, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                0, 0, GridBagConstraints.CENTER, 1.0f, 0.0f, new Insets(0, 5, 0, 10));
        searchWrapper.add(searchTextField, Constraints.getGridBagConstraints());

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchContainer.setOpaque(false);
        searchContainer.add(searchWrapper);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
                1, 0, GridBagConstraints.CENTER, new Insets(0, 0, 10, 0));
        mainPanel.add(searchContainer, Constraints.getGridBagConstraints());
    }


    /**
     * Esegue la logica di ricerca.
     * <p>
     * Recupera il testo inserito dall'utente (gestendo il caso del placeholder),
     * invoca il {@link UserController} per cercare utenti (sviluppatori o admin) nel backend
     * e aggiorna la tabella dei risultati con i dati ottenuti.
     * </p>
     */
    public void performSearch() {

        String text = searchTextField.getText();
        String userEmail = text.equals(PLACEHOLDER) ? "" : text;

        boolean success = UserController.getInstance().searchDevOrAdminByEmail(userEmail);

        if(!success)
            return;

        updateResultsTable(UserController.getInstance().getUsersEmails());
    }


    /**
     * Aggiorna la tabella dei risultati con la lista di email fornite.
     * <p>
     * Ricostruisce il modello della tabella, imposta i renderer per lo stile del testo
     * e assegna l'editor personalizzato {@link AddMemberActionCellEditor} alla colonna "Azione".
     * Questo editor trasforma la cella in un pulsante cliccabile per aggiungere l'utente.
     * </p>
     *
     * @param emails Lista di stringhe contenente le email degli utenti trovati.
     */
    public void updateResultsTable(List<String> emails) {

        resultsPanel.removeAll();

        String[] cols = {"Email", "Azione"};
        Object[][] data = new Object[emails.size()][2];

        for (int i = 0; i < emails.size(); i++) {
            data[i][0] = emails.get(i);
            data[i][1] = "Aggiungi";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1; }
        };

        JTable table = new JTable(model);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setForeground(new Color(0, 120, 215));
        renderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        table.getColumnModel().getColumn(1).setCellEditor(
                new AddMemberActionCellEditor(mainFrame, table, this)
        );

        table.setRowHeight(35);
        resultsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}