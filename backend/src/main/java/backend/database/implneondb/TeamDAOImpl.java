package backend.database.implneondb;

import backend.database.dao.TeamDAO;
import backend.dto.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementazione del Data Access Object (DAO) per la gestione dei team.
 * <p>
 * Questa classe gestisce l'interazione diretta con il database per le operazioni sui gruppi di lavoro.
 * Implementa le funzionalità CRUD (ricerca, creazione) e la gestione delle relazioni molti-a-molti
 * tra utenti e team (aggiunta/rimozione membri), oltre alla generazione di report statistici specifici per team.
 * </p>
 */
@Repository
public class TeamDAOImpl implements TeamDAO {

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
    public TeamDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Cerca i team nel database filtrando per nome e progetto.
     * <p>
     * Esegue una query SQL con operatore ILIKE per una ricerca case-insensitive
     * all'interno di uno specifico progetto.
     * </p>
     *
     * @param teamName  Il nome (o parte di esso) da cercare.
     * @param projectId L'identificativo del progetto contesto della ricerca.
     * @return Una lista di {@code TeamDTO} contenente i team trovati.
     * @throws SQLException In caso di errori durante l'esecuzione della query.
     */
    public List<TeamDTO> searchTeamsByNameAndProject(String teamName, Integer projectId) throws SQLException{


        List<TeamDTO> searchResult;

        String query = "SELECT T.* FROM Team T WHERE team_name ILIKE ? AND project_id = ?;";

        String toSearch = "%" + teamName + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, toSearch);
            statement.setInt(2, projectId);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            ProjectDTO relatedProject = new ProjectDTO();
            relatedProject.setId(projectId);

            while (rs.next()) {

                TeamDTO foundedTeam = new TeamDTO();

                foundedTeam.setId(rs.getInt("team_id"));
                foundedTeam.setName(rs.getString("team_name"));
                foundedTeam.setProject(relatedProject);

                searchResult.add(foundedTeam);

            }

            rs.close();

        }

        return searchResult;

    }

    /**
     * Crea un nuovo team nel database.
     * <p>
     * Inserisce un nuovo record nella tabella 'Team' associandolo al progetto specificato.
     * </p>
     *
     * @param teamToCreate Il DTO contenente i dati del team da creare.
     * @throws SQLException In caso di errori durante l'inserimento nel database.
     */
    public void createTeam(TeamDTO teamToCreate) throws SQLException{

        String query = "INSERT INTO Team (team_name, project_id) VALUES "+
                       "(?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, teamToCreate.getName());
            statement.setInt(2, teamToCreate.getProject().getId());

            statement.executeUpdate();

        }

    }

    /**
     * Aggiunge un membro a un team.
     * <p>
     * Inserisce un record nella tabella di relazione 'Works_in'.
     * Utilizza una subquery per recuperare l'ID dell'utente partendo dalla sua email.
     * </p>
     *
     * @param teamId L'ID del team a cui aggiungere il membro.
     * @param email  L'email dell'utente da aggiungere.
     * @return {@code true} se l'inserimento ha successo (1 riga aggiunta), {@code false} altrimenti.
     * @throws SQLException In caso di errori o violazioni di vincoli (es utente già presente).
     */
    public boolean addMemberToTeam(Integer teamId, String email) throws SQLException{

        String query = "INSERT INTO Works_in (team_id, user_id) VALUES "+
                       "(?, (SELECT user_id FROM User_ U WHERE U.email = ?))";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setString(2, email);

            int rowsAdded = statement.executeUpdate();

            return rowsAdded > 0;
        }

    }

    /**
     * Rimuove un membro da un team.
     * <p>
     * Elimina il record corrispondente dalla tabella di relazione 'Works_in'.
     * Utilizza una subquery per identificare l'utente tramite email.
     * </p>
     *
     * @param teamId L'ID del team da cui rimuovere il membro.
     * @param email  L'email dell'utente da rimuovere.
     * @return {@code true} se la rimozione ha successo, {@code false} se l'associazione non esisteva.
     * @throws SQLException In caso di errori durante l'operazione.
     */
    public boolean removeMemberFromTeam(Integer teamId, String email) throws SQLException{

        String query = "DELETE FROM Works_in " +
                       "WHERE team_id = ? " +
                       "AND user_id = (SELECT user_id FROM User_ WHERE email = ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setString(2, email);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;
        }

    }

    /**
     * Genera un report statistico mensile per il team specificato.
     * <p>
     * Esegue una query complessa che incrocia le tabelle Team, Project, Works_in, User_ e Issue.
     * Recupera le issue gestite dai membri del team che sono state create o risolte nel mese e anno specificati.
     * Riutilizza metodi statici di {@link ProjectDAOImpl} per aggregare i dati e calcolare le medie.
     * </p>
     *
     * @param teamId L'ID del team per cui generare il report.
     * @param month  Il nome del mese in italiano (es "gennaio").
     * @param year   L'anno di riferimento sotto forma di stringa (es. "2023").
     * @return Un oggetto {@code StatisticDTO} contenente i dati del report.
     * @throws SQLException In caso di errori durante l'elaborazione della query.
     */
    public StatisticDTO generateMonthlyReport(Integer teamId, String month, String year) throws SQLException{

        StatisticDTO reportGenerated;

        String query = "SELECT DISTINCT I.issue_id, I.resolver_id, I.report_time, I.resolution_time, U1.email AS resolver_email " +
                       "FROM Team T " +
                       "JOIN Project P ON T.project_id = P.project_id " +
                       "JOIN Works_in W ON T.team_id = W.team_id " +
                       "JOIN User_ U1 ON W.user_id = U1.user_id " +
                       "JOIN Issue I ON (I.project_id = P.project_id AND I.resolver_id = U1.user_id) " +
                       "WHERE T.team_id = ? " +
                       "AND ( " +
                       "  (EXTRACT(MONTH FROM I.report_time) = ? AND EXTRACT(YEAR FROM I.report_time) = ?) " +
                       "  OR " +
                       "  (EXTRACT(MONTH FROM I.resolution_time) = ? AND EXTRACT(YEAR FROM I.resolution_time) = ?) " +
                       ")";

        ArrayList<Integer> numIssueSolvedForDev = new ArrayList<>();
        ArrayList<Integer> devAlreadyFoundedIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, teamId);
            statement.setInt(2, stringMonthToDBInt(month));
            statement.setInt(3, Integer.parseInt(year));
            statement.setInt(4, stringMonthToDBInt(month));
            statement.setInt(5, Integer.parseInt(year));


            ResultSet rs = statement.executeQuery();

            reportGenerated = new StatisticDTO();

            Duration totalDuration = Duration.ZERO;

            int resolvedCount = 0;

            while (rs.next()) {

                IssueDTO foundedIssue = new IssueDTO();

                foundedIssue.setId(rs.getInt("issue_id"));


                int resolverId = rs.getInt("resolver_id");

                //rs.wasNull() checks if last column was NULL
                if (!rs.wasNull() && resolverId >= 0) {

                    ProjectDAOImpl.resolverHandler(devAlreadyFoundedIds, resolverId, reportGenerated, foundedIssue, rs, numIssueSolvedForDev);


                } else {

                    foundedIssue.setAssignedDeveloper(null);

                }


                Timestamp reportTimestamp = rs.getTimestamp("report_time");
                Timestamp resolutionTimestamp = rs.getTimestamp("resolution_time");

                foundedIssue.setReportDate(new Date(reportTimestamp.getTime()));

                if (resolutionTimestamp != null) {

                    foundedIssue.setResolutionDate(new Date(resolutionTimestamp.getTime()));

                    Duration issueDuration = Duration.between(reportTimestamp.toInstant(), resolutionTimestamp.toInstant());
                    totalDuration = totalDuration.plus(issueDuration);
                    resolvedCount++;

                    reportGenerated.getClosedIssues().add(foundedIssue);

                    ProjectDAOImpl.manageDevAverageDuration(reportGenerated, foundedIssue, issueDuration, numIssueSolvedForDev);

                }else{

                    ProjectDAOImpl.manageDevNumOpenIssues(reportGenerated, foundedIssue);

                }


            }

            ProjectDAOImpl.countingAndAverageCalculation(reportGenerated, numIssueSolvedForDev, resolvedCount, totalDuration);

            rs.close();

        }


        return reportGenerated;

    }

    /**
     * Metodo di utilità per convertire il nome del mese in italiano nel corrispondente intero.
     *
     * @param monthName Il nome del mese (es. "gennaio", "Gennaio").
     * @return L'intero corrispondente (1-12) o 0 se il nome non è valido.
     */
    private int stringMonthToDBInt(String monthName) {

        return switch(monthName.toLowerCase()) {
            case "gennaio" -> 1;
            case "febbraio" -> 2;
            case "marzo" -> 3;
            case "aprile" -> 4;
            case "maggio" -> 5;
            case "giugno" -> 6;
            case "luglio" -> 7;
            case "agosto" -> 8;
            case "settembre" -> 9;
            case "ottobre" -> 10;
            case "novembre" -> 11;
            case "dicembre" -> 12;
            default -> 0;
        };
    }

}
