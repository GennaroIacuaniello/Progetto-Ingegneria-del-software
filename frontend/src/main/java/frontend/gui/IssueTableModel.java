package frontend.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Modello dati per la tabella delle segnalazioni (Issue).
 * <p>
 * Questa classe estende {@link AbstractTableModel} e funge da ponte tra i dati grezzi (una matrice bidimensionale)
 * e il componente grafico {@link javax.swing.JTable}.
 * Definisce la struttura della tabella (nomi e tipi delle colonne) e gestisce l'accessibilità delle celle
 * (quali sono modificabili/cliccabili).
 * </p>
 */
public class IssueTableModel extends AbstractTableModel {

    /**
     * Array contenente le intestazioni delle colonne.
     */
    protected String[] columnNames;

    /**
     * Matrice bidimensionale contenente i dati effettivi da visualizzare.
     * <p>
     * Ogni riga rappresenta una Issue, ogni colonna un attributo o un'azione.
     * </p>
     */
    protected final Object[][] data;

    /**
     * Costruttore del modello.
     * <p>
     * Inizializza i nomi delle colonne e memorizza i dati passati dal controller.
     * </p>
     *
     * @param data La matrice di oggetti che popolerà la tabella (es. {Titolo, Icona}).
     */
    public IssueTableModel(Object[][] data) {

        setColumnNames();
        this.data = data;
    }

    /**
     * Definisce i nomi delle colonne della tabella.
     * <p>
     * Configura la tabella con due colonne:
     * <ol>
     * <li><b>TITOLO ISSUE:</b> Il testo descrittivo della segnalazione.</li>
     * <li><b>VEDI ISSUE:</b> La colonna contenente il pulsante di azione.</li>
     * </ol>
     * </p>
     */
    protected void setColumnNames() {

        columnNames = new String[]{"TITOLO ISSUE", "VEDI ISSUE"};
    }

    /**
     * Restituisce il numero totale di colonne.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Restituisce il numero totale di righe (numero di issue).
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Restituisce il nome della colonna all'indice specificato.
     * Utilizzato dalla JTable per popolare l'header.
     */
    @Override
    public String getColumnName(int columnName) {
        return columnNames[columnName];
    }

    /**
     * Restituisce il valore contenuto nella cella specificata.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * Restituisce la classe degli oggetti contenuti nella colonna specificata.
     * <p>
     * Questo metodo è fondamentale per il rendering corretto:
     * <ul>
     * <li>Se l'indice è 1 (seconda colonna), restituisce {@link IconButton}.class. Questo segnala alla JTable
     * di utilizzare il renderer/editor personalizzato per i pulsanti invece di visualizzare il testo "toString()".</li>
     * <li>Per le altre colonne, usa il comportamento di default.</li>
     * </ul>
     * </p>
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex == 1)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    /**
     * Determina se una cella è modificabile (interattiva).
     * <p>
     * Restituisce {@code true} solo per la colonna 1 (quella dei pulsanti), permettendo all'utente
     * di cliccare l'icona per aprire i dettagli. La colonna del titolo (0) è in sola lettura.
     * </p>
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }
}