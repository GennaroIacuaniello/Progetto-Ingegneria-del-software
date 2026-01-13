package frontend.gui;

public class ProjectTableModelAdmin extends ProjectTableModelDeveloper{

    /**
     * Costruttore del modello tabella Admin.
     * <p>
     * Inizializza il modello richiamando il costruttore della superclasse {@link ProjectTableModelDeveloper},
     * che si occupa di salvare i dati grezzi. Successivamente, il metodo {@link #setColumnNames()} (sovrascritto qui)
     * verrà chiamato per definire le colonne specifiche.
     * </p>
     *
     * @param data La matrice di dati contenente le informazioni sui progetti.
     */
    public ProjectTableModelAdmin(Object[][] data) {
        super(data);
    }

    /**
     * Definisce le intestazioni delle colonne per la vista Amministratore.
     * <p>
     * Configura la tabella con il set più completo di azioni disponibili:
     * <ol>
     * <li><b>ID PROGETTO</b> (Dato)</li>
     * <li><b>NOME PROGETTO</b> (Dato)</li>
     * <li><b>SEGNALA ISSUE</b> (Azione)</li>
     * <li><b>ISSUE SEGNALATE</b> (Azione)</li>
     * <li><b>ISSUE ASSEGNATE</b> (Azione)</li>
     * <li><b>VEDI TUTTE LE ISSUE:</b> (Nuova) Permette di vedere tutte le segnalazioni del progetto, indipendentemente dall'autore o dall'assegnatario.</li>
     * <li><b>GESTISCI TEAM:</b> (Nuova) Permette di aggiungere/rimuovere team e membri dal progetto.</li>
     * </ol>
     * </p>
     */
    protected void setColumnNames() {

        columnNames = new String[]{"ID PROGETTO", "NOME PROGETTO", "SEGNALA ISSUE", "ISSUE SEGNALATE", "ISSUE ASSEGNATE", "VEDI TUTTE LE ISSUE", "GESTISCI TEAM"};
    }

    /**
     * Restituisce la classe degli oggetti nelle colonne.
     * <p>
     * Specifica che le colonne dalla 2 alla 6 (inclusa) contengono {@link IconButton}.
     * Questo intervallo copre tutte le 5 azioni disponibili per l'admin.
     * </p>
     *
     * @param columnIndex L'indice della colonna.
     * @return {@code IconButton.class} per le colonne di azione, altrimenti la classe base.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if (columnIndex >= 2 && columnIndex <= 6)
            return IconButton.class;
        else
            return super.getColumnClass(columnIndex);
    }

    /**
     * Determina quali celle sono interattive.
     * <p>
     * Rende editabili (cliccabili) le colonne dalla 2 alla 6, permettendo all'utente
     * di interagire con tutti i pulsanti di azione. Le colonne 0 (ID) e 1 (Nome) rimangono di sola lettura.
     * </p>
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 2 && columnIndex <= 6;
    }
}