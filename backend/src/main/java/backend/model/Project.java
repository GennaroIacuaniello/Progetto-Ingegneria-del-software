package backend.model;

import backend.exception.InvalidDeveloper;
import backend.exception.InvalidTeam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta un progetto nel sistema.
 * <p>
 * Utilizza {@code @Data} per la gestione automatica dei metodi standard e {@code @NoArgsConstructor}
 * per il costruttore vuoto. Include un costruttore manuale per la logica di validazione.
 * </p>
 */
@Data
@NoArgsConstructor
public class Project {

    /**
     * Identificativo univoco del progetto.
     */
    private int id;

    /**
     * Nome del progetto.
     */
    private String name;

    /**
     * Lista delle issue associate al progetto.
     */
    private List<Issue> issues;

    /**
     * Lista dei team che lavorano sul progetto.
     */
    private List<Team> teams;

    /**
     * Lista globale degli sviluppatori assegnati al progetto.
     */
    private List<Developer> developers;

    /**
     * Costruttore parametrico con validazione.
     * <p>
     * Verifica che le liste di team e sviluppatori non siano nulle o vuote al momento della creazione.
     * </p>
     *
     * @param id         ID del progetto.
     * @param name       Nome del progetto.
     * @param teams      Lista dei team iniziali.
     * @param developers Lista degli sviluppatori iniziali.
     * @throws InvalidTeam      Se la lista dei team è nulla o vuota.
     * @throws InvalidDeveloper Se la lista degli sviluppatori è nulla o vuota.
     */
    @SuppressWarnings("unused")
    public Project(int id, String name, List<Team> teams, List<Developer> developers) throws InvalidTeam, InvalidDeveloper {

        if( teams == null || teams.isEmpty() )
            throw new InvalidTeam("Teams cannot be null or empty");

        if( developers == null || developers.isEmpty() )
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.id = id;

        this.name = name;

        this.issues = null;

        this.teams = new ArrayList<>();

        this.teams.addAll(teams);

        this.developers = developers;
    }

}