package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;

/**
 * Editor di cella predefinito per le issue segnalate (Reported Issue) lato Utente.
 * <p>
 * Questa classe estende {@link DefaultCellEditor} e viene utilizzata per trasformare una cella della tabella
 * in un pulsante interattivo (icona). Quando l'utente clicca sulla cella, viene attivato questo editor
 * che apre la finestra di dettaglio della segnalazione ({@link ShowReportedIssueUser}).
 * </p>
 * <p>
 * Funge anche da classe base per editor più specifici (come quelli per Developer o Admin),
 * che possono sovrascrivere il metodo {@link #getCellEditorValue()} per aprire finestre diverse.
 * </p>
 */
public class IconCellEditorReportedIssueUser extends DefaultCellEditor {

    /**
     * Il componente grafico (pulsante icona) utilizzato per l'editing.
     */
    private final IconButton button;

    /**
     * Riferimento alla tabella che contiene questo editor.
     * Necessario per determinare quale riga è stata selezionata.
     */
    protected final JTable parentTable;

    /**
     * Riferimento al frame principale, usato per posizionare le finestre di dialogo modali.
     */
    protected JFrame  mainFrame;

    /**
     * Costruttore dell'editor.
     * <p>
     * Configura l'editor per attivarsi con un singolo click.
     * Inizializza il pulsante con l'icona specificata e imposta un listener per terminare
     * l'editing immediatamente dopo il click, scatenando l'azione definita in {@link #getCellEditorValue()}.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione.
     * @param url       Il percorso dell'immagine/icona da visualizzare.
     * @param width     Larghezza dell'icona.
     * @param height    Altezza dell'icona.
     * @param table     La tabella JTable a cui l'editor appartiene.
     */
    public IconCellEditorReportedIssueUser(JFrame mainFrame, String url, int width, int height, JTable table) {

        // Passa un JTextField fittizio al costruttore di DefaultCellEditor per soddisfare i requisiti della superclasse
        super(new JTextField());

        this.parentTable = table;
        this.mainFrame = mainFrame;

        // Imposta che basta un solo click per avviare l'azione (di default a volte ne servono 2)
        setClickCountToStart(1);

        button = new IconButton(url, width, height);

        // Al click sul bottone, ferma l'editing. Questo triggera automaticamente getCellEditorValue().
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Configura e restituisce il componente da visualizzare durante l'editing della cella.
     * <p>
     * Imposta il colore di sfondo del pulsante per adattarsi allo stato di selezione della riga.
     * </p>
     *
     * @param table      La tabella che richiede l'editor.
     * @param value      Il valore della cella.
     * @param isSelected Se la cella è attualmente selezionata.
     * @param row        L'indice della riga.
     * @param column     L'indice della colonna.
     * @return Il componente {@link IconButton} configurato.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        if (isSelected)
            button.setBackground(table.getSelectionBackground());
        else
            button.setBackground(table.getBackground());

        return button;
    }

    /**
     * Esegue l'azione principale quando l'utente clicca sull'icona.
     * <p>
     * La logica implementata è:
     * <ol>
     * <li>Identifica l'issue corrispondente alla riga selezionata.</li>
     * <li>Imposta l'issue corrente nel {@link IssueController}.</li>
     * <li>Richiede i dettagli completi dell'issue al backend.</li>
     * <li>Se il recupero ha successo, apre la finestra di dialogo {@link ShowReportedIssueUser}.</li>
     * </ol>
     * </p>
     *
     * @return {@code null}, poiché l'azione non produce un valore da salvare nel modello della tabella, ma apre solo una vista.
     */
    @Override
    public Object getCellEditorValue() {

        IssueController.getInstance().setIssue(IssueController.getInstance().getIssueFromIndex(parentTable.getSelectedRow()));
        boolean success = IssueController.getInstance().getIssueById();

        if(!success)
            return null;

        ShowReportedIssueUser dialog = new ShowReportedIssueUser(mainFrame);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);

        return null;
    }
}