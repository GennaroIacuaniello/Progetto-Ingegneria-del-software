package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Editor di cella personalizzato per la rimozione di un membro dal team.
 * <p>
 * Questa classe estende {@link DefaultCellEditor} e viene utilizzata nella tabella di gestione membri.
 * Renderizza la cella come un pulsante testuale (simile a un link) colorato di rosso per indicare
 * un'azione distruttiva.
 * Al click, non elimina direttamente il membro, ma recupera l'email associata e apre una
 * finestra di dialogo di conferma ({@link ConfirmDeleteMemberDialog}).
 * </p>
 */
public class RemoveMemberCellEditor extends DefaultCellEditor {

    /**
     * Il componente bottone utilizzato per l'interazione.
     */
    private final JButton button;

    /**
     * Riferimento al frame principale (per il posizionamento dei dialog).
     */
    private final JFrame mainFrame;

    /**
     * Riferimento alla tabella contenente l'elenco dei membri.
     */
    private final JTable table;

    /**
     * Riferimento al dialog genitore, necessario per aggiornare la lista dopo l'eliminazione.
     */
    private final ManageMembersDialog parentDialog;

    /**
     * Etichetta del pulsante (es. "Rimuovi").
     */
    private String label;

    /**
     * Costruttore dell'editor.
     * <p>
     * Inizializza il pulsante con uno stile specifico:
     * </p>
     * <ul>
     * <li>Sfondo trasparente e nessun bordo (aspetto testuale).</li>
     * <li>Colore del testo rosso (RGB 220, 53, 69) per indicare pericolo/eliminazione.</li>
     * <li>Cursore a mano per indicare cliccabilità.</li>
     * </ul>
     * <p>
     * Configura l'azione al click:
     * </p>
     * <ol>
     * <li>Identifica la riga selezionata.</li>
     * <li>Interrompe l'editing della cella.</li>
     * <li>Estrae l'email del membro dalla colonna 0.</li>
     * <li>Apre {@link ConfirmDeleteMemberDialog} passando l'email e il riferimento al dialog padre per il refresh.</li>
     * </ol>
     *
     * @param mainFrame    Il frame principale dell'applicazione.
     * @param table        La tabella dei membri.
     * @param parentDialog Il dialog di gestione membri che contiene la tabella.
     */
    public RemoveMemberCellEditor(JFrame mainFrame, JTable table, ManageMembersDialog parentDialog) {

        super(new JCheckBox());
        this.mainFrame = mainFrame;
        this.table = table;
        this.parentDialog = parentDialog;
        this.button = new JButton();

        // Configurazione stile "Link pericoloso"
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setForeground(new Color(220, 53, 69)); // Rosso warning
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                fireEditingStopped(); // Chiude l'editor

                // Recupera l'email (chiave primaria logica) dalla prima colonna
                String email = (String) table.getValueAt(row, 0);

                // Apre la conferma
                ConfirmDeleteMemberDialog confirm = new ConfirmDeleteMemberDialog(mainFrame, email, parentDialog);
                confirm.setVisible(true);
            }
        });
    }

    /**
     * Configura il componente editor prima che venga mostrato.
     *
     * @param t La tabella.
     * @param v Il valore della cella (il testo "Rimuovi membro").
     * @param s Se selezionato.
     * @param r Indice riga.
     * @param c Indice colonna.
     * @return Il bottone configurato.
     */
    @Override
    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
        label = (v == null) ? "" : v.toString();
        button.setText(label);
        return button;
    }

    /**
     * Restituisce il valore contenuto nella cella.
     */
    @Override
    public Object getCellEditorValue() {
        return label;
    }
}