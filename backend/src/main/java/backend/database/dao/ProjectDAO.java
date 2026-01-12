package backend.database.dao;

import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per il Data Access Object (DAO) relativo alla gestione dei progetti.
 * <p>
 * Definisce il contratto per le operazioni di persistenza sui progetti,
 * incluse la ricerca per nome, la creazione di nuovi progetti e
 * la generazione delle statistiche globali per la dashboard.
 * </p>
 */
public interface ProjectDAO {

    /**
     * Cerca i progetti il cui nome corrisponde o contiene la stringa specificata.
     *
     * @param projectName Il nome (o parte di esso) del progetto da cercare.
     * @return Una lista di {@code ProjectDTO} che soddisfano i criteri di ricerca.
     * @throws SQLException In caso di errori durante l'accesso al database.
     */
    List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException;

    /**
     * Crea un nuovo progetto nel database.
     * <p>
     * Persiste le informazioni contenute nel DTO (es nome del progetto)
     * creando un nuovo record nella tabella dedicata.
     * </p>
     *
     * @param projectToCreate Il DTO contenente i dati del progetto da creare.
     * @throws SQLException In caso di errori durante l'inserimento nel database.
     */
    void createProject(ProjectDTO projectToCreate) throws SQLException;

    /**
     * Genera e recupera i dati statistici per la dashboard principale.
     * <p>
     * Aggrega informazioni globali del sistema (come il numero totale di progetti,
     * issue aperte, ecc) per fornire una panoramica all'utente.
     * </p>
     *
     * @return Un oggetto {@code StatisticDTO} contenente i dati riepilogativi.
     * @throws SQLException In caso di errori durante il calcolo delle statistiche.
     */
    StatisticDTO generateDashboard() throws SQLException;

}
