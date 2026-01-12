package backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) per le statistiche del sistema.
 * <p>
 * Questa classe aggrega dati complessi per la generazione di dashboard e report.
 * Contiene metriche sulle performance degli sviluppatori, conteggi sulle issue aperte/chiuse
 * e tempi medi di risoluzione.
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
public class StatisticDTO {

    /**
     * Lista degli sviluppatori inclusi nelle statistiche.
     */
    private List<UserDTO> developers = new ArrayList<>();

    /**
     * Lista dettagliata delle segnalazioni attualmente aperte.
     */
    private List<IssueDTO> openIssues = new ArrayList<>();

    /**
     * Lista dettagliata delle segnalazioni risolte (chiuse).
     */
    private List<IssueDTO> closedIssues = new ArrayList<>();

    /**
     * Lista parallela a {@code developers}: contiene il numero di issue aperte per ogni sviluppatore.
     */
    private List<Integer> numOpenIssues = new ArrayList<>();

    /**
     * Lista parallela a {@code developers}: contiene il numero di issue chiuse per ogni sviluppatore.
     */
    private List<Integer> numClosedIssues = new ArrayList<>();

    /**
     * Conteggio totale delle segnalazioni non ancora assegnate a nessuno sviluppatore.
     */
    private Integer numIssuesNotAssigned = 0;

    /**
     * Lista parallela a {@code developers}: contiene la durata media di risoluzione per ogni sviluppatore.
     * <p>
     * L'annotazione {@code @JsonFormat(shape = JsonFormat.Shape.STRING)} assicura che la durata
     * venga serializzata come stringa ISO-8601 (es. "PT1H30M") per compatibilità con il frontend.
     * </p>
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Duration> averageResolutionDurations = new ArrayList<>();

    /**
     * Durata media globale di risoluzione delle issue in tutto il sistema (o nel contesto del report).
     * Serializzata come stringa ISO-8601.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration totalAverageResolutionDuration = Duration.ZERO;

}