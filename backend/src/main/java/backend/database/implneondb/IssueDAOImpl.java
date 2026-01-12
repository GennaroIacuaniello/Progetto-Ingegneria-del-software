package backend.database.implneondb;

import backend.database.dao.IssueDAO;
import backend.dto.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementazione del Data Access Object (DAO) per la gestione delle segnalazioni (Issue).
 * <p>
 * Questa classe gestisce l'interazione diretta con il database (configurato per NeonDB/PostgreSQL)
 * tramite JDBC. Si occupa di tradurre le operazioni logiche definite nell'interfaccia {@link IssueDAO}
 * in query SQL concrete per la creazione, ricerca, aggiornamento e assegnazione delle segnalazioni.
 * </p>
 */
@Repository
public class IssueDAOImpl implements IssueDAO {

    /**
     * Fonte dati per la connessione al database.
     * Viene iniettata da Spring e gestisce il pool di connessioni verso il DB persistente.
     */
    private final DataSource dataSource;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param dataSource Il DataSource configurato per l'accesso al database.
     */
    public IssueDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Registra una nuova segnalazione nel database.
     * <p>
     * Esegue una query di inserimento (INSERT) popolando la tabella 'Issue'.
     * Gestisce la conversione dei tipi Java in tipi SQL (es. Enum in stringhe per i tipi custom di Postgres,
     * gestione dei BLOB per le immagini, gestione dei NULL).
     * Imposta automaticamente la data di creazione al timestamp corrente.
     * </p>
     *
     * @param issueToReport Il DTO contenente i dati della segnalazione da creare.
     * @throws SQLException In caso di errori di connessione o esecuzione della query SQL.
     */
    public void reportIssue(IssueDTO issueToReport) throws SQLException{

        String query = "INSERT INTO Issue (title, issue_description, issue_priority, issue_image, issue_type, issue_status, tags, report_time, reporter_id, resolver_id, project_id) VALUES "+
                        "(?, ?, ?, ?, ?::IssueType, ?::IssueStatus, ?, CURRENT_TIMESTAMP, ?, ?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, issueToReport.getTitle());
            statement.setString(2, issueToReport.getDescription());
            statement.setInt(3, issueToReport.getPriority());

            if (issueToReport.getImage() != null) {
                statement.setBytes(4, issueToReport.getImage());
            } else {
                statement.setNull(4, Types.BINARY);
            }

            statement.setString(5, issueToReport.getType().toString());
            statement.setString(6, issueToReport.getStatus().toString());
            statement.setString(7, issueToReport.getTags());
            statement.setInt(8, issueToReport.getReportingUser().getId());
            statement.setNull(9, Types.INTEGER);
            statement.setInt(10, issueToReport.getRelatedProject().getId());


            statement.executeUpdate();


        }

    }

    /**
     * Esegue una ricerca dinamica delle segnalazioni basata su filtri opzionali.
     * <p>
     * Costruisce dinamicamente la query SQL (StringBuilder) aggiungendo clausole WHERE
     * solo per i parametri di ricerca effettivamente forniti (non null o non vuoti).
     * Utilizza operatori come ILIKE per ricerche testuali case-insensitive su titolo e tag.
     * </p>
     *
     * @param issueToSearch DTO contenente i criteri di filtro (titolo, stato, tipo, priorità, tags).
     * @param resolverId    ID dello sviluppatore assegnatario (opzionale).
     * @param reporterId    ID dell'utente reporter (opzionale).
     * @param projectId     ID del progetto (obbligatorio) a cui le issue devono appartenere.
     * @return Una lista di {@code IssueDTO} (in formato ridotto per liste) trovate.
     * @throws SQLException In caso di errori durante l'esecuzione della query dinamica.
     */
    public List<IssueDTO> searchIssues(IssueDTO issueToSearch, Integer resolverId, Integer reporterId, Integer projectId) throws SQLException{

        List<IssueDTO> searchResult;

        StringBuilder query = new StringBuilder("SELECT issue_id, title, issue_status FROM issue WHERE ");
        List<Object> searchParam = new ArrayList<>();

        query.append("project_id = ?");
        searchParam.add(projectId);

        if(resolverId != null){
            query.append(" AND resolver_id = ?");
            searchParam.add(resolverId);
        }

        if(reporterId != null){
            query.append(" AND reporter_id = ?");
            searchParam.add(reporterId);
        }

        if (issueToSearch.getTitle() != null && !issueToSearch.getTitle().isEmpty()) {
            query.append(" AND title ILIKE ?");
            searchParam.add("%" + issueToSearch.getTitle() + "%");
        }
        if (issueToSearch.getStatus() != null) {
            query.append(" AND issue_status = ?::IssueStatus");
            searchParam.add(issueToSearch.getStatus().toString());
        }
        if (issueToSearch.getType() != null) {
            query.append(" AND issue_type = ?::IssueType");
            searchParam.add(issueToSearch.getType().toString());
        }
        if (issueToSearch.getPriority() != null) {
            query.append(" AND issue_priority = ?");
            searchParam.add(issueToSearch.getPriority());
        }
        if (issueToSearch.getTags() != null && !issueToSearch.getTags().isEmpty()) {
            query.append(" AND tags ILIKE ?");
            searchParam.add("%" + issueToSearch.getTags() + "%");
        }


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {


            for(int i = 0; i < searchParam.size(); i++){

                statement.setObject( i + 1, searchParam.get(i));

            }

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                IssueDTO issueFounded = new IssueDTO();

                issueFounded.setId(rs.getInt("issue_id"));
                issueFounded.setTitle(rs.getString("title"));
                issueFounded.setStatus(IssueStatusDTO.valueOf(rs.getString("issue_status")));


                searchResult.add(issueFounded);


            }

            rs.close();

        }

        return searchResult;

    }

    /**
     * Recupera i dettagli completi di una specifica segnalazione.
     * <p>
     * Esegue una query di JOIN tra le tabelle 'Issue', 'User_' (per reporter e resolver)
     * e 'Project' per costruire un oggetto DTO completo di tutte le relazioni.
     * </p>
     *
     * @param issueId L'identificativo univoco della segnalazione.
     * @return Il DTO completo della segnalazione, o {@code null} se l'ID non esiste.
     * @throws SQLException In caso di errori di accesso al database.
     */
    public IssueDTO getIssueById(Integer issueId) throws SQLException{

        IssueDTO searchResult = null;

        String query = "SELECT I.*, " + // I.* to take all columns of table issues
                "U1.email AS reporter_email, " +
                "U2.email AS resolver_email, " +
                "P.project_name " +
                "FROM Issue I " +
                "JOIN User_ U1 ON I.reporter_id = U1.user_id " +
                "LEFT JOIN User_ U2 ON I.resolver_id = U2.user_id " +
                "JOIN Project P ON I.project_id = P.project_id " +
                "WHERE issue_id = ?;";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, issueId);

            ResultSet rs = statement.executeQuery();



            while (rs.next()) {

                searchResult = new IssueDTO();

                searchResult.setId(rs.getInt("issue_id"));
                searchResult.setTitle(rs.getString("title"));
                searchResult.setDescription(rs.getString("issue_description"));
                searchResult.setPriority(rs.getInt("issue_priority"));
                searchResult.setImage(rs.getBytes("issue_image"));
                searchResult.setType(IssueTypeDTO.valueOf(rs.getString("issue_type")));
                searchResult.setStatus(IssueStatusDTO.valueOf(rs.getString("issue_status")));
                searchResult.setTags(rs.getString("tags"));

                searchResult.setReportDate(new Date(rs.getTimestamp("report_time").getTime()));

                Timestamp resolutionTimestamp = rs.getTimestamp("resolution_time");

                if(resolutionTimestamp != null)
                    searchResult.setResolutionDate(new Date(resolutionTimestamp.getTime()));

                UserDTO reporter = new UserDTO();
                reporter.setId(rs.getInt("reporter_id"));
                reporter.setEmail(rs.getString("reporter_email"));

                searchResult.setReportingUser(reporter);


                int resolverId = rs.getInt("resolver_id");

                //rs.wasNull() checks if last column was NULL
                if (!rs.wasNull() && resolverId >= 0) {

                    UserDTO resolver = new UserDTO();
                    resolver.setId(resolverId);
                    resolver.setEmail(rs.getString("resolver_email"));
                    searchResult.setAssignedDeveloper(resolver);

                } else {

                    searchResult.setAssignedDeveloper(null);

                }

                ProjectDTO relatedProject = new ProjectDTO();
                relatedProject.setId(rs.getInt("project_id"));
                relatedProject.setName(rs.getString("project_name"));

                searchResult.setRelatedProject(relatedProject);


            }

            rs.close();

        }

        return searchResult;

    }

    /**
     * Aggiorna lo stato di una segnalazione esistente.
     * <p>
     * Modifica il campo 'issue_status' della tabella 'Issue' per il record specificato.
     * </p>
     *
     * @param id        L'ID della segnalazione da aggiornare.
     * @param newStatus Il nuovo stato da applicare.
     * @return {@code true} se almeno una riga è stata aggiornata, {@code false} altrimenti.
     * @throws SQLException In caso di errori durante l'aggiornamento.
     */
    public boolean updateStatus(Integer id, IssueStatusDTO newStatus) throws SQLException{

        String query = "UPDATE Issue SET issue_status = ?::IssueStatus WHERE issue_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newStatus.toString());
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();

            // Return true if update success, false otherwise
            return rowsUpdated > 0;
        }

    }

    /**
     * Assegna una segnalazione a uno sviluppatore e ne aggiorna lo stato.
     * <p>
     * Esegue una query complessa che:
     * 1. Cerca l'ID dell'utente basandosi sull'email fornita.
     * 2. Aggiorna la tabella 'Issue' impostando il resolver_id trovato.
     * 3. Aggiorna automaticamente lo stato della issue ad 'ASSIGNED'.
     * Utilizza la clausola RETURNING per ottenere immediatamente i dati dell'utente assegnato.
     * </p>
     *
     * @param id            L'ID della segnalazione da assegnare.
     * @param resolverEmail L'email dello sviluppatore a cui assegnare la task.
     * @return UserDTO con i dati dello sviluppatore assegnato se l'operazione ha successo, altrimenti null.
     * @throws SQLException In caso di errori durante l'aggiornamento o la selezione.
     */
    public UserDTO assignIssueToDeveloperByEmail(Integer id, String resolverEmail) throws SQLException{

        String query = "UPDATE Issue I " +
                       "SET resolver_id = U.user_id, issue_status = 'ASSIGNED'::IssueStatus " +
                       "FROM User_ U " +
                       "WHERE I.issue_id = ? AND U.email = ? " +
                       "RETURNING U.user_id, U.email";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.setString(2, resolverEmail);

            ResultSet rs = statement.executeQuery();


            if (rs.next()) {

                UserDTO assignedUser = new UserDTO();

                assignedUser.setId(rs.getInt("user_id"));
                assignedUser.setEmail(rs.getString("email"));

                rs.close();

                return assignedUser;
            }

            rs.close();

        }

        return null;

    }

}
