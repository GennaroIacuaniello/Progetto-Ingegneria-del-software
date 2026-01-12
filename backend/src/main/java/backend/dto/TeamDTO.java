package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) per rappresentare un team di sviluppo.
 * <p>
 * Un team è un gruppo di lavoro associato a uno specifico progetto.
 * Questa classe trasporta i dati del team e la lista dei suoi membri.
 * </p>
 * <p>
 * <strong>Utilizzo di Lombok:</strong><br>
 * <ul>
 * <li>{@link Data @Data}: Genera automaticamente getter, setter, toString, equals e hashCode.</li>
 * <li>{@link NoArgsConstructor @NoArgsConstructor}: Genera il costruttore vuoto.</li>
 * <li>{@link AllArgsConstructor @AllArgsConstructor}: Genera il costruttore con tutti gli argomenti.</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {

    /**
     * Identificativo univoco del team.
     */
    private Integer id;

    /**
     * Nome del team.
     */
    private String name;

    /**
     * Il progetto a cui il team è assegnato.
     */
    private ProjectDTO project;

    /**
     * Lista degli sviluppatori che compongono il team.
     */
    private List<UserDTO> developers;

}