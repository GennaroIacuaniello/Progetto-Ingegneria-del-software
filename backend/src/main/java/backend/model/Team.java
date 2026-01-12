package backend.model;

import backend.exception.InvalidDeveloper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta un team di sviluppo.
 * <p>
 * Utilizza {@code @Data} per getter, setter, equals, hashCode e toString,
 * e {@code @NoArgsConstructor} per il costruttore di default.
 * </p>
 */
@Data
@NoArgsConstructor
public class Team {

    /**
     * Nome del team.
     */
    private String name;

    /**
     * Progetto a cui il team appartiene.
     */
    private Project project;

    /**
     * Lista degli sviluppatori membri del team.
     */
    private List<Developer> developers;

    /**
     * Costruttore parametrico con validazione.
     * <p>
     * Assicura che il team venga creato con almeno uno sviluppatore valido.
     * </p>
     *
     * @param name       Nome del team.
     * @param project    Progetto di riferimento.
     * @param developers Lista degli sviluppatori membri.
     * @throws InvalidDeveloper Se la lista degli sviluppatori è nulla o vuota.
     */
    @SuppressWarnings("unused")
    public Team(String name, Project project, List<Developer> developers) throws InvalidDeveloper {

        if (developers == null || developers.isEmpty())
            throw new InvalidDeveloper("Developers cannot be null or empty");

        this.name = name;
        this.project = project;

        this.developers = new ArrayList<>();

        this.developers.addAll(developers);

    }

}