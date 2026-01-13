package frontend.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Modello dati base per la tabella dei progetti (lato Utente Standard).
 * <p>
 * Questa classe estende {@link AbstractTableModel} e definisce come i dati dei progetti vengono
 * presentati in una {@link javax.swing.JTable}.
 * Configura la vista per un utente base, prevedendo 4 colonne:
 * <ol>
 * <li><b>ID PROGETTO:</b> Identificativo numerico.</li>
 * <li><b>NOME PROGETTO:</b> Nome del progetto.</li>
 * <li><b>SEGNALA ISSUE:</b> Pulsante per aprire la segnalazione.</li>
 * <li><b>ISSUE SEGNALATE:</b> Pulsante per vedere le proprie segnalazioni.</li>
 * </ol>
 * Funge da superclasse per {@link ProjectTableModelDeveloper} e {@link ProjectTableModelAdmin}.
 * </p>
 */
public class ProjectTableModelUser extends AbstractTableModel {

    /**
     * Array contenente le intestazioni delle colonne.
     */
    protected String[] columnNames;

    /**
     * Matrice bidimensionale contenente i dati effettivi (Righe x Colonne).
     */
    protected final Object[][] data;

    /**
     * Costruttore del modello.
     * <p>
     * Inizializza i nomi delle colonne e memorizza i dati ricevuti dal controller.
     * </p>
     *
     * @param data La matrice di oggetti che rappresenta l'elenco dei progetti.
     */
    public ProjectTableModelUser(Object[][] data) {

        setColumnNames();
        this.data = data;
    }

    /**
     * Configura i nomi delle colonne per l'utente standard.
     * <p>
     * Questo metodo è {@code protected} per consentire alle sottoclassi di sovrascriverlo
     * e aggiungere ulteriori colonne (es. "Issue Assegnate" per i Developer).
     * </p>
     */
    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE"};
    }

    /**
     * Restituisce il numero totale di colonne.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Restituisce il numero totale di righe (numero di progetti).
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Restituisce il nome della colonna all'indice specificato.
     * Usato dalla JTable per disegnare l'header.
     */
    @Override
    public String getColumnName(int columnName) {
        return columnNames[columnName];
    }

    /**
     * Restituisce il valore contenuto nella cella specifica.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * Restituisce la classe degli oggetti contenuti nella colonna specificata.
     * <p>
     * Questo metodo è cruciale per il rendering:
     * <ul>
     * <li>Se l'indice è 2 o 3 (le colonne azione), restituisce {@link IconButton}.class.
     * Questo segnala alla tabella di usare il renderer personalizzato per i pulsanti.</li>
     * <li>Per le colonne 0 e 1, usa il comportamento standard (visualizzazione testo).</li>
     * </ul>
     * </p>
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 3)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    /**
     * Determina se una cella è modificabile (interattiva).
     * <p>
     * Rende editabili solo le colonne 2 e 3, permettendo all'utente di cliccare sui pulsanti.
     * Le colonne informative (ID e Nome) sono di sola lettura.
     * </p>
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 3;
    }
}