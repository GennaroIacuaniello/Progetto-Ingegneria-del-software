package frontend.gui;

import frontend.controller.ProjectController;

import javax.swing.*;
import java.awt.*;

/**
 * Editor di cella base per la tabella dei progetti (lato Utente).
 * <p>
 * Questa classe estende {@link DefaultCellEditor} e trasforma specifiche celle della tabella progetti
 * in pulsanti interattivi (icone). Gestisce la logica di navigazione quando un utente clicca su un'icona.
 * <br>
 * Per un utente standard, le azioni disponibili sono limitate a:
 * <ul>
 * <li><b>SEGNALA ISSUE:</b> Apre la schermata per creare una nuova segnalazione.</li>
 * <li><b>ISSUE SEGNALATE:</b> Apre la lista delle segnalazioni fatte dall'utente in quel progetto.</li>
 * </ul>
 * Questa classe viene estesa da {@link ProjectIconCellEditorDeveloper} e {@link ProjectIconCellEditorAdmin}
 * per aggiungere ulteriori funzionalità.
 * </p>
 */
class ProjectIconCellEditorUser extends DefaultCellEditor {

    /**
     * Il pulsante icona che viene mostrato quando la cella è in modalità "editing" (cliccata).
     */
    private final IconButton button;

    /**
     * Indice della riga attualmente selezionata.
     */
    protected int selectedRow;

    /**
     * Riferimento alla tabella dei progetti.
     */
    protected final JTable parentTable;

    /**
     * Riferimento al pannello centrale, usato per cambiare la schermata visualizzata (navigazione).
     */
    protected HomePanelUser homePanel;

    /**
     * Riferimento al frame principale.
     */
    protected JFrame  mainFrame;

    /**
     * Costruttore dell'editor.
     * <p>
     * Configura l'editor per attivarsi con un singolo click.
     * </p>
     *
     * @param mainFrame Il frame principale.
     * @param homePanel Il pannello contenitore per la navigazione.
     * @param url       URL dell'icona da mostrare.
     * @param width     Larghezza icona.
     * @param height    Altezza icona.
     * @param table     La tabella di riferimento.
     */
    public ProjectIconCellEditorUser(JFrame mainFrame, HomePanelUser homePanel, String url, int width, int height, JTable table) {

        // Passa un JTextField fittizio al costruttore padre (necessario per DefaultCellEditor)
        super(new JTextField());

        this.parentTable = table;
        this.homePanel = homePanel;
        this.mainFrame = mainFrame;

        // Basta un click per attivare l'azione
        setClickCountToStart(1);

        button = new IconButton(url, width, height);

        // Al click, ferma l'editing per scatenare getCellEditorValue()
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Prepara il componente grafico (bottone) per la visualizzazione.
     * <p>
     * Memorizza l'indice della riga selezionata (fondamentale per recuperare l'ID del progetto successivamente)
     * e adatta lo sfondo del pulsante alla selezione della tabella.
     * </p>
     *
     * @param table      La tabella.
     * @param value      Il valore della cella.
     * @param isSelected Se la cella è selezionata.
     * @param row        L'indice della riga.
     * @param column     L'indice della colonna.
     * @return Il componente {@link IconButton}.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.selectedRow = row;

        if (isSelected)
            button.setBackground(table.getSelectionBackground());
        else
            button.setBackground(table.getBackground());

        return button;
    }

    /**
     * Esegue l'azione di navigazione vera e propria.
     * <p>
     * Questo metodo viene chiamato quando l'editing termina (al click del bottone).
     * La logica è la seguente:
     * <ol>
     * <li>Recupera il nome della colonna per capire quale azione l'utente vuole eseguire.</li>
     * <li>Imposta il progetto attivo nel {@link ProjectController} prendendo ID (colonna 0) e Nome (colonna 1) dalla riga selezionata.</li>
     * <li>In base all'azione, sostituisce il pannello centrale della Home con la nuova vista (ReportIssue o SearchReportedIssue).</li>
     * </ol>
     * </p>
     *
     * @return {@code null}, poiché l'azione è la navigazione, non la modifica dei dati.
     */
    @Override
    public Object getCellEditorValue() {

        String action = parentTable.getColumnName(parentTable.getEditingColumn());

        // Setup del contesto del progetto
        ProjectController.getInstance().setProjectWithValues((Integer)parentTable.getValueAt(selectedRow, 0),
                (String)parentTable.getValueAt(selectedRow, 1));

        // Routing della navigazione
        switch (action) {

            case "SEGNALA ISSUE":
                homePanel.setContentPanel(new ReportIssueUser(mainFrame, homePanel));
                break;

            case "ISSUE SEGNALATE":
                homePanel.setContentPanel(new SearchReportedIssuePageUser(mainFrame, homePanel));
                break;
        }

        return null;
    }
}