package backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) per rappresentare un utente del sistema.
 * <p>
 * Questa classe veicola le informazioni relative agli utenti, includendo dati di autenticazione,
 * ruolo e le relazioni con altre entità del sistema (issue segnalate, issue assegnate, progetti e team).
 * </p>
 * <p>
 * <strong>Utilizzo di Lombok:</strong><br>
 * </p>
 * <ul>
 * <li>{@link Data @Data}: Genera automaticamente getter, setter, toString, equals e hashCode.</li>
 * <li>{@link NoArgsConstructor @NoArgsConstructor}: Genera il costruttore vuoto.</li>
 * <li>{@link AllArgsConstructor @AllArgsConstructor}: Genera il costruttore con tutti gli argomenti.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * Identificativo univoco dell'utente.
     */
    private Integer id;

    /**
     * Indirizzo email dell'utente, utilizzato come username per il login.
     */
    private String email;

    /**
     * Password dell'utente (generalmente hashata o vuota quando inviata al client per sicurezza).
     */
    private String password;

    /**
     * Ruolo dell'utente nel sistema.
     * <p>
     * Valori previsti:
     * <ul>
     * <li>0: Guest (utente base)</li>
     * <li>1: Developer (sviluppatore)</li>
     * <li>2: Admin (amministratore)</li>
     * </ul>
     * </p>
     */
    private Integer role; //0 guest, 1 developer, 2 admin

    /**
     * Lista delle segnalazioni (Issue) aperte da questo utente.
     * Inizializzata come lista vuota di default.
     */
    private List<IssueDTO> reportedIssues = new ArrayList<>();

    /**
     * Lista delle segnalazioni assegnate a questo utente per la risoluzione.
     * Rilevante solo per utenti con ruolo Developer o Admin.
     */
    private List<IssueDTO> assignedIssues = new ArrayList<>();      //only developer or admin

    /**
     * Lista dei progetti a cui l'utente partecipa.
     */
    private List<ProjectDTO> projects = new ArrayList<>();

    /**
     * Lista dei team di cui l'utente è membro.
     */
    private List<TeamDTO> teams = new ArrayList<>();

}