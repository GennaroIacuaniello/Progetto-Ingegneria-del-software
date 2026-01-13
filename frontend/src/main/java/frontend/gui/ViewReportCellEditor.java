package frontend.gui;

import frontend.controller.TeamController;

import javax.swing.*;
import java.awt.*;

/**
 * Editor personalizzato per la cella "Visualizza Report" nella tabella dei team.
 * <p>
 * Questa classe estende {@link DefaultCellEditor} per trasformare una cella della tabella
 * in un pulsante interattivo.
 * <br>
 * Il suo scopo specifico è gestire la navigazione: quando l'utente clicca sull'icona/testo
 * "View Report" in una riga specifica, questa classe intercetta l'evento, recupera l'ID del team
 * e reindirizza l'utente alla pagina delle statistiche ({@link TeamReportPage}).
 * </p>
 */
public class ViewReportCellEditor extends DefaultCellEditor {

    /**
     * Il componente pulsante che gestisce l'interazione effettiva.
     */
    private final JButton button;

    /**
     * Etichetta per memorizzare il valore della cella.
     */
    private String label;

    /**
     * Costruttore dell'editor.
     * <p>
     * Configura un {@link JButton} "invisibile" (trasparente) che si attiva al click sulla cella.
     * Definisce la logica dell'azione (ActionListener):
     * <ol>
     * <li>Recupera l'indice della riga selezionata.</li>
     * <li>Estrae l'ID del Team dalla prima colonna (indice 0) della tabella.</li>
     * <li>Imposta il team corrente nel {@link TeamController} usando l'ID recuperato.</li>
     * <li>Ferma la modalità di editing della cella.</li>
     * <li>Effettua il cambio di schermata nel pannello Home, caricando {@link TeamReportPage}.</li>
     * </ol>
     * </p>
     *
     * @param table            La tabella su cui opera l'editor (necessaria per leggere l'ID del team).
     * @param homePanel        Il pannello Home principale (necessario per cambiare la vista centrale).
     * @param manageTeamsPanel Il pannello di gestione team (passato al costruttore della nuova pagina per permettere il tasto "Indietro").
     */
    public ViewReportCellEditor(JTable table, HomePanelUser homePanel, ManageTeamsPanel manageTeamsPanel) {
        super(new JCheckBox()); // Trick standard di Swing per usare un editor personalizzato
        this.button = new JButton();

        // Configurazione stilistica per rendere il bottone simile a una label/icona
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {

                // 1. Setta il team attivo nel controller
                TeamController.getInstance().setTeamWithId((int) table.getValueAt(row, 0));

                // 2. Ferma l'editing
                fireEditingStopped();

                // 3. Naviga alla pagina del report
                homePanel.setContentPanel(new TeamReportPage(manageTeamsPanel));

            }
        });
    }

    /**
     * Configura il componente editor prima che venga mostrato.
     * <p>
     * Questo metodo viene chiamato da Swing quando l'utente inizia a cliccare sulla cella.
     * Prepara il pulsante impostando il testo corretto basato sul valore della cella.
     * </p>
     *
     * @param table      La tabella inviante.
     * @param value      Il valore della cella.
     * @param isSelected Se la cella è selezionata.
     * @param row        Indice riga.
     * @param column     Indice colonna.
     * @return Il componente (JButton) configurato.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    /**
     * Restituisce il valore contenuto nella cella dopo l'editing.
     *
     * @return Il valore della cella (l'etichetta).
     */
    @Override
    public Object getCellEditorValue() {
        return label;
    }
}