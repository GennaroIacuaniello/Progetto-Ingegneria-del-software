package frontend.gui;

import frontend.controller.ProjectController;
import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;

/**
 * Editor di cella personalizzato che funge da pulsante testuale per la gestione dei membri.
 * <p>
 * Questa classe estende {@link DefaultCellEditor} e viene utilizzata per rendere cliccabile
 * una cella contenente testo (es. "Gestisci Membri"). L'aspetto visivo è simile a un link
 * (testo blu, cursore a mano).
 * Al click, recupera l'ID del team dalla riga corrente e apre la finestra di dialogo {@link ManageMembersDialog}.
 * </p>
 */
public class ManageMembersTextCellEditor extends DefaultCellEditor {

    /**
     * Il componente effettivo utilizzato per l'editing (un bottone stilizzato come testo).
     */
    private final JButton button;

    /**
     * Il testo da visualizzare sul pulsante (il valore della cella).
     */
    private String label;

    /**
     * Riferimento al frame principale per gestire le finestre di dialogo.
     */
    private final JFrame mainFrame;

    /**
     * Riferimento alla tabella per accedere ai dati della riga selezionata.
     */
    private final JTable table;

    /**
     * Costruttore dell'editor.
     * <p>
     * Inizializza il componente {@link JButton} rimuovendo le decorazioni standard (bordi, sfondo)
     * per farlo sembrare un semplice testo cliccabile.
     * Configura l'azione al click:
     * </p>
     * <ol>
     * <li>Identifica la riga selezionata nella tabella.</li>
     * <li>Recupera l'ID del team (presumibilmente nella colonna 0, indice 0).</li>
     * <li>Aggiorna il {@link TeamController} con il team selezionato.</li>
     * <li>Interrompe l'editing della cella.</li>
     * <li>Apre la finestra {@link ManageMembersDialog}.</li>
     * </ol>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param table     La tabella che contiene questo editor.
     */
    public ManageMembersTextCellEditor(JFrame mainFrame, JTable table) {

        super(new JCheckBox()); // Hack standard per estendere DefaultCellEditor con componenti custom
        this.mainFrame = mainFrame;
        this.table = table;


        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));


        this.button.setForeground(new Color(0, 120, 215)); // Blu link standard
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));

        this.button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                // Recupera l'ID del Team dalla prima colonna (indice 0) della riga cliccata
                TeamController.getInstance().setTeamWithId((int) table.getValueAt(row, 0));

                fireEditingStopped(); // Chiude l'editor

                ManageMembersDialog membersDialog = new ManageMembersDialog(mainFrame);
                membersDialog.setVisible(true);
            }
        });
    }

    /**
     * Configura il componente editor prima che venga mostrato.
     * <p>
     * Imposta il testo del pulsante in base al valore contenuto nella cella.
     * </p>
     *
     * @param table      La tabella che richiede l'editor.
     * @param value      Il valore della cella.
     * @param isSelected Se la cella è selezionata.
     * @param row        Indice riga.
     * @param column     Indice colonna.
     * @return Il componente {@code button} configurato.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    /**
     * Restituisce il valore finale della cella dopo l'editing.
     * <p>
     * In questo caso restituisce semplicemente l'etichetta originale, poiché l'azione
     * principale è l'apertura della dialog e non la modifica del dato in tabella.
     * </p>
     */
    @Override
    public Object getCellEditorValue() {
        return label;
    }
}