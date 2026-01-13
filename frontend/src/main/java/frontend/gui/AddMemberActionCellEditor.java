package frontend.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Editor di celle personalizzato per la tabella di ricerca e aggiunta membri.
 * <p>
 * Questa classe estende {@link DefaultCellEditor} per visualizzare e gestire un pulsante ("Action Button")
 * all'interno di una cella di una {@link JTable}.
 * Quando l'utente clicca sulla cella, viene attivato il pulsante che apre una finestra di conferma
 * ({@link ConfirmAddDialog}) per aggiungere l'utente selezionato (identificato dall'email nella riga).
 * </p>
 */
public class AddMemberActionCellEditor extends DefaultCellEditor {

    /**
     * Il componente pulsante visualizzato durante l'editing della cella.
     */
    private final JButton button;

    /**
     * Il testo (etichetta) da visualizzare sul pulsante.
     */
    private String label;

    /**
     * Costruttore dell'editor.
     * <p>
     * Inizializza il pulsante definendone lo stile (trasparente, testo blu, cursore a mano) per sembrare un link o un'azione moderna.
     * Aggiunge un {@link java.awt.event.ActionListener} che:
     * <ol>
     * <li>Identifica la riga selezionata nella tabella.</li>
     * <li>Estrae l'email dell'utente dalla prima colonna (indice 0).</li>
     * <li>Ferma l'editing della cella.</li>
     * <li>Apre il dialog di conferma {@link ConfirmAddDialog}.</li>
     * </ol>
     * </p>
     *
     * @param mainFrame    Il frame principale dell'applicazione, usato come parent per i dialoghi modali.
     * @param table        La tabella su cui opera l'editor.
     * @param parentDialog Il dialog padre ({@link AddMemberDialog}) da cui è partita l'azione.
     */
    public AddMemberActionCellEditor(JFrame mainFrame, JTable table, AddMemberDialog parentDialog) {
        super(new JCheckBox()); // Usa un JCheckBox come placeholder per il costruttore della superclasse

        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.setBorderPainted(false);
        this.button.setContentAreaFilled(false);
        this.button.setForeground(new Color(0, 120, 215)); // Blu stile link
        this.button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                // Recupera l'email dalla prima colonna della riga selezionata
                String email = (String) table.getValueAt(row, 0);

                // Notifica che l'editing è terminato per salvare lo stato
                fireEditingStopped();

                // Apre la finestra di conferma
                ConfirmAddDialog confirm = new ConfirmAddDialog(mainFrame, email, parentDialog);
                confirm.setVisible(true);
            }
        });
    }

    /**
     * Configura il componente dell'editor prima che venga visualizzato nella tabella.
     * <p>
     * Imposta il testo del pulsante in base al valore della cella.
     * </p>
     *
     * @param table      La tabella che richiede l'editor.
     * @param value      Il valore contenuto nella cella da editare.
     * @param isSelected Indica se la cella è attualmente selezionata.
     * @param row        L'indice della riga della cella.
     * @param column     L'indice della colonna della cella.
     * @return Il componente {@link JButton} configurato.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    /**
     * Restituisce il valore corrente contenuto nell'editor.
     *
     * @return La stringa che rappresenta l'etichetta del pulsante.
     */
    @Override
    public Object getCellEditorValue() {
        return label;
    }
}