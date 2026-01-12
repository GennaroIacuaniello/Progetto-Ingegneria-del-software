package backend.database.implneondb;

import backend.database.dao.ProjectDAO;
import backend.dto.IssueDTO;
import backend.dto.ProjectDTO;
import backend.dto.StatisticDTO;
import backend.dto.UserDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementazione del Data Access Object (DAO) per la gestione dei progetti.
 * <p>
 * Questa classe gestisce l'interazione diretta con il database per le operazioni sui progetti.
 * Oltre alle operazioni standard CRUD (creazione e ricerca), implementa una logica complessa
 * per la generazione della dashboard statistica, aggregando dati su issue, sviluppatori e tempi di risoluzione.
 * </p>
*/
@Repository
public class ProjectDAOImpl implements ProjectDAO {

    /**
     * Fonte dati per la connessione al database.
     * Gestisce il pool di connessioni JDBC verso il database persistente.
     */
    private final DataSource dataSource;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param dataSource Il DataSource configurato per l'accesso al database.
     */
    public ProjectDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Cerca i progetti nel database in base al nome.
     * <p>
     * Esegue una query SQL utilizzando l'operatore ILIKE per una ricerca case-insensitive
     * che trova corrispondenze parziali nel nome del progetto.
     * </p>
     *
     * @param projectName Il nome (o parte di esso) da cercare.
     * @return Una lista di {@code ProjectDTO} contenente i progetti trovati.
     * @throws SQLException In caso di errori durante l'esecuzione della query.
     */
    public List<ProjectDTO> searchProjectsByName(String projectName) throws SQLException{


        List<ProjectDTO> searchResult;

        String query = "SELECT P.* FROM Project P WHERE project_name ILIKE ?;";

        String toSearch = "%" + projectName + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, toSearch);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                searchResult.add(new ProjectDTO( rs.getInt("project_id"), rs.getString("project_name") ));


            }

            rs.close();

        }

        return searchResult;

    }

    /**
     * Crea un nuovo progetto nel database.
     * <p>
     * Esegue un'istruzione INSERT per salvare il nome del nuovo progetto.
     * </p>
     *
     * @param projectToCreate Il DTO contenente i dati del progetto da creare.
     * @throws SQLException In caso di errori durante l'inserimento nel database.
     */
    public void createProject(ProjectDTO projectToCreate) throws SQLException{

        String query = "INSERT INTO Project (project_name) VALUES "+
                       "(?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, projectToCreate.getName());

            statement.executeUpdate();

        }

    }

    /**
     * Genera le statistiche globali per la dashboard del sistema.
     * <p>
     * Esegue un'analisi approfondita recuperando tutte le issue e i relativi risolutori.
     * Calcola metriche come:
     * <ul>
     * <li>Numero di issue aperte e chiuse.</li>
     * <li>Issue non assegnate.</li>
     * <li>Tempo medio di risoluzione totale e per singolo sviluppatore.</li>
     * <li>Carico di lavoro per sviluppatore (issue aperte vs chiuse).</li>
     * </ul>
     * Utilizza metodi helper statici per organizzare la logica di elaborazione dei risultati.
     * </p>
     *
     * @return Un oggetto {@code StatisticDTO} popolato con tutti i dati aggregati.
     * @throws SQLException In caso di errori durante l'interrogazione del database.
     */
    public StatisticDTO generateDashboard() throws SQLException{

        StatisticDTO dashboardData;

        String query = "SELECT DISTINCT I.issue_id, I.resolver_id, I.report_time, I.resolution_time, U1.email AS resolver_email " +
                       "FROM Issue I LEFT JOIN User_ U1 ON  I.resolver_id = U1.user_id ";

        ArrayList<Integer> numIssueSolvedForDev = new ArrayList<>();
        ArrayList<Integer> devAlreadyFoundedIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();

            dashboardData = new StatisticDTO();

            Duration totalDuration = Duration.ZERO;

            int resolvedCount = 0;

            while (rs.next()) {

                IssueDTO foundedIssue = new IssueDTO();

                foundedIssue.setId(rs.getInt("issue_id"));


                int resolverId = rs.getInt("resolver_id");

                //rs.wasNull() checks if last column was NULL
                if (!rs.wasNull() && resolverId >= 0) {

                    resolverHandler(devAlreadyFoundedIds, resolverId, dashboardData, foundedIssue, rs, numIssueSolvedForDev);


                } else {

                    foundedIssue.setAssignedDeveloper(null);
                    Integer prevNumIssuesNotAssigned = dashboardData.getNumIssuesNotAssigned();

                    dashboardData.setNumIssuesNotAssigned(prevNumIssuesNotAssigned + 1);

                }


                Timestamp reportTimestamp = rs.getTimestamp("report_time");
                Timestamp foundedResolutionTimestamp = rs.getTimestamp("resolution_time");

                foundedIssue.setReportDate(new java.util.Date(reportTimestamp.getTime()));

                if (foundedResolutionTimestamp != null) {

                    foundedIssue.setResolutionDate(new Date(foundedResolutionTimestamp.getTime()));

                    dashboardData.getClosedIssues().add(foundedIssue);

                    Duration issueDuration = Duration.between(reportTimestamp.toInstant(), foundedResolutionTimestamp.toInstant());
                    totalDuration = totalDuration.plus(issueDuration);
                    resolvedCount++;

                    manageDevAverageDuration(dashboardData, foundedIssue, issueDuration, numIssueSolvedForDev);

                }else{

                    manageDevNumOpenIssues(dashboardData, foundedIssue);

                }


            }

            countingAndAverageCalculation(dashboardData, numIssueSolvedForDev, resolvedCount, totalDuration);


            rs.close();

        }


        return dashboardData;


    }

    /**
     * Gestisce l'associazione e l'inizializzazione dei dati relativi allo sviluppatore (risolutore).
     * <p>
     * Se lo sviluppatore è già stato incontrato durante l'iterazione, lo associa alla issue corrente.
     * Se è nuovo, crea un nuovo oggetto UserDTO, lo aggiunge alle liste di tracciamento e inizializza
     * i contatori per le statistiche individuali.
     * </p>
     *
     * @param devAlreadyFoundedIds Lista degli ID degli sviluppatori già processati.
     * @param resolverId           ID del risolutore corrente.
     * @param dashboardData        DTO delle statistiche in fase di popolamento.
     * @param foundedIssue         La issue corrente in elaborazione.
     * @param rs                   Il ResultSet posizionato sulla riga corrente.
     * @param numIssueSolvedForDev Lista contatore per le issue risolte per ogni dev.
     * @throws SQLException In caso di errori nel recupero dei dati dal ResultSet.
     */
    public static void resolverHandler(List<Integer> devAlreadyFoundedIds, int resolverId, StatisticDTO dashboardData, IssueDTO foundedIssue, ResultSet rs, List<Integer> numIssueSolvedForDev) throws SQLException {

        if(devAlreadyFoundedIds.contains(resolverId)){
            for(UserDTO dev: dashboardData.getDevelopers())
                if(dev.getId() == resolverId){
                    foundedIssue.setAssignedDeveloper(dev);
                    break;
                }
        } else {

            UserDTO resolver = new UserDTO();
            resolver.setId(resolverId);
            resolver.setEmail(rs.getString("resolver_email"));

            foundedIssue.setAssignedDeveloper(resolver);
            devAlreadyFoundedIds.add(resolverId);


            dashboardData.getDevelopers().add(resolver);

            dashboardData.getAverageResolutionDurations().add(Duration.ZERO);

            dashboardData.getNumClosedIssues().add(0);
            dashboardData.getNumOpenIssues().add(0);


            numIssueSolvedForDev.add(0);
        }
    }

    /**
     * Aggiorna le metriche temporali per uno sviluppatore quando una issue è risolta.
     * <p>
     * Accumula la durata della risoluzione per il calcolo successivo della media e
     * incrementa il contatore delle issue risolte per lo specifico sviluppatore.
     * </p>
     *
     * @param dashboardData        DTO delle statistiche.
     * @param foundedIssue         La issue risolta corrente.
     * @param issueDuration        La durata calcolata per la risoluzione della issue.
     * @param numIssueSolvedForDev Lista contatore per le issue risolte.
     */
    public static void manageDevAverageDuration(StatisticDTO dashboardData, IssueDTO foundedIssue, Duration issueDuration, List<Integer> numIssueSolvedForDev) {
        int devIndex = dashboardData.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

        if (devIndex != -1) {

            Duration currentSum = dashboardData.getAverageResolutionDurations().get(devIndex);

            dashboardData.getAverageResolutionDurations().set(devIndex, currentSum.plus(issueDuration));

            numIssueSolvedForDev.set(devIndex, numIssueSolvedForDev.get(devIndex) + 1);

        }
    }

    /**
     * Gestisce il conteggio delle issue aperte per uno sviluppatore.
     * <p>
     * Incrementa il contatore delle issue aperte per lo sviluppatore assegnato
     * e aggiunge la issue alla lista globale delle issue aperte.
     * </p>
     *
     * @param dashboardData DTO delle statistiche.
     * @param foundedIssue  La issue aperta corrente.
     */
    public static void manageDevNumOpenIssues(StatisticDTO dashboardData, IssueDTO foundedIssue) {
        int devIndex = dashboardData.getDevelopers().indexOf(foundedIssue.getAssignedDeveloper());

        if (devIndex != -1) {

            Integer prevNumOpenIssues = dashboardData.getNumOpenIssues().get(devIndex);

            dashboardData.getNumOpenIssues().set(devIndex, prevNumOpenIssues + 1);

        }

        foundedIssue.setResolutionDate(null);
        dashboardData.getOpenIssues().add(foundedIssue);
    }

    /**
     * Finalizza i calcoli statistici al termine dell'elaborazione di tutte le issue.
     * <p>
     * Calcola la durata media di risoluzione per ogni sviluppatore (dividendo la somma totale per il numero di issue)
     * e la durata media globale del sistema.
     * </p>
     *
     * @param dashboardData        DTO delle statistiche completato con i dati grezzi.
     * @param numIssueSolvedForDev Lista con il numero di issue risolte per ogni dev.
     * @param resolvedCount        Numero totale di issue risolte nel sistema.
     * @param totalDuration        Durata totale accumulata di tutte le risoluzioni.
     */
    public static void countingAndAverageCalculation(StatisticDTO dashboardData, List<Integer> numIssueSolvedForDev, int resolvedCount, Duration totalDuration) {
        for (int i = 0; i < dashboardData.getAverageResolutionDurations().size(); i++) {

            int count = numIssueSolvedForDev.get(i);

            dashboardData.getNumClosedIssues().set(i, count);

            if (count > 0) {
                Duration totalSum = dashboardData.getAverageResolutionDurations().get(i);
                dashboardData.getAverageResolutionDurations().set(i, totalSum.dividedBy(count));
            }

        }

        if (resolvedCount > 0) {

            Duration averageDuration = totalDuration.dividedBy(resolvedCount);

            dashboardData.setTotalAverageResolutionDuration(averageDuration);
        }
    }

}
