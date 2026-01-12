package frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) per rappresentare un progetto.
 * <p>
 * Contiene le informazioni strutturali di un progetto, incluse le liste
 * delle issue associate, dei team di lavoro e degli sviluppatori coinvolti.
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
public class ProjectDTO {

    /**
     * Identificativo univoco del progetto.
     */
    private Integer id;

    /**
     * Nome del progetto.
     */
    private String name;

    /**
     * Lista delle segnalazioni (Issue) associate al progetto.
     */
    private List<IssueDTO> issues;

    /**
     * Lista dei team che lavorano sul progetto.
     */
    private List<TeamDTO> teams;

    /**
     * Lista degli sviluppatori assegnati al progetto.
     */
    private List<UserDTO> developers;

    /**
     * Costruttore personalizzato per creare un ProjectDTO con solo ID e nome.
     * Utile per operazioni di ricerca leggera o visualizzazione in elenchi.
     *
     * @param id   L'identificativo del progetto.
     * @param name Il nome del progetto.
     */
    public ProjectDTO(Integer id, String name) {

        this.id = id;
        this.name = name;
    }

}