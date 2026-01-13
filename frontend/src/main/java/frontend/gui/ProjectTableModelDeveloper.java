package frontend.gui;

/**
 * Modello dati per la tabella dei progetti, specifico per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link ProjectTableModelUser}. Mantiene le informazioni di base e le azioni
 * dell'utente standard (segnalazione e visualizzazione delle proprie segnalazioni), ma aggiunge
 * una colonna fondamentale per il workflow di sviluppo: "ISSUE ASSEGNATE".
 * </p>
 */
public class ProjectTableModelDeveloper extends ProjectTableModelUser{

    /**
     * Costruttore del modello tabella Developer.
     * <p>
     * Inizializza il modello richiamando il costruttore della superclasse per gestire i dati grezzi.
     * </p>
     *
     * @param data La matrice di oggetti contenente i dati dei progetti.
     */
    public ProjectTableModelDeveloper(Object[][] data) {
        super(data);
    }

    /**
     * Definisce le intestazioni delle colonne per la vista Sviluppatore.
     * <p>
     * Configura la tabella con le seguenti colonne:
     * </p>
     * <ol>
     * <li><b>ID PROGETTO</b> (Dato)</li>
     * <li><b>NOME PROGETTO</b> (Dato)</li>
     * <li><b>SEGNALA ISSUE</b> (Azione)</li>
     * <li><b>ISSUE SEGNALATE</b> (Azione)</li>
     * <li><b>ISSUE ASSEGNATE:</b> (Nuova) Permette allo sviluppatore di vedere le issue che deve risolvere.</li>
     * </ol>
     */
    @Override
    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE", "ISSUE ASSEGNATE"};
    }

    /**
     * Restituisce la classe degli oggetti nelle colonne.
     * <p>
     * Specifica che le colonne dalla 2 alla 4 (inclusa) devono essere trattate come {@link IconButton}.
     * Questo dice alla JTable di usare il renderer personalizzato per disegnare i bottoni invece del testo.
     * </p>
     *
     * @param columnIndex L'indice della colonna.
     * @return {@code IconButton.class} per le colonne di azione, altrimenti la classe base.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 4)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    /**
     * Determina quali celle sono interattive.
     * <p>
     * Rende cliccabili (editabili) le colonne 2, 3 e 4 (le azioni).
     * Le colonne 0 (ID) e 1 (Nome) rimangono in sola lettura.
     * </p>
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 4;
    }
}