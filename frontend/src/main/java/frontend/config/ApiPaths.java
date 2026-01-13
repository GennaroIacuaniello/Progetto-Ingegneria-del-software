package frontend.config;

/**
 * Classe di utilità che definisce le costanti per i percorsi base (endpoint) dell'API REST.
 * <p>
 * Questa classe centralizza le stringhe relative agli URL delle risorse del backend
 * (es issues, users, projects, teams). È dichiarata {@code final} per impedirne l'estensione
 * e possiede un costruttore privato per impedirne l'istanziazione, in quanto deve essere
 * utilizzata esclusivamente tramite i suoi campi statici.
 * </p>
 */
public final class ApiPaths {

    /**
     * Costruttore privato per impedire l'istanziazione della classe.
     * <p>
     * Essendo una classe di sole costanti statiche, non è necessario creare oggetti di tipo {@code ApiPaths}.
     * </p>
     */
    private ApiPaths() {}

    /**
     * Percorso base per le operazioni relative alle segnalazioni (Issue).
     * <p>
     * Utilizzato per costruire URL come "/issues/{id}", "/issues/search", ecc.
     * </p>
     */
    public static final String ISSUES = "/issues/";

    /**
     * Percorso base per le operazioni relative agli utenti.
     * <p>
     * Utilizzato per la ricerca di guest, developer o admin.
     * </p>
     */
    public static final String USERS = "/users/";

    /**
     * Percorso base per le operazioni relative ai progetti.
     * <p>
     * Utilizzato per creare progetti, cercarli ecc.
     * </p>
     */
    public static final String PROJECTS = "/projects/";

    /**
     * Percorso base per le operazioni relative ai team.
     * <p>
     * Utilizzato per la creazione di team, la gestione dei membri e la generazione dei report.
     * </p>
     */
    public static final String TEAMS = "/teams/";

}
