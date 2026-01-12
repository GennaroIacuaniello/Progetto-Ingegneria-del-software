package backend.database.implneondb;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del Data Access Object (DAO) per la gestione degli utenti.
 * <p>
 * Questa classe gestisce l'interazione diretta con il database (configurato per NeonDB/PostgreSQL)
 * tramite JDBC per tutte le operazioni relative agli utenti: registrazione, login (ricerca per email)
 * e ricerche avanzate per l'assegnazione di task o la gestione dei team.
 * </p>
 */
@Repository
public class UserDAOImpl implements UserDAO {

    /**
     * Fonte dati per la connessione al database.
     * Gestisce il pool di connessioni verso il DB persistente.
     */
    private final DataSource dataSource;

    /**
     * Costante per il nome della colonna identificativa dell'utente nel database.
     * Utilizzata per evitare errori di digitazione nel recupero dei dati dal ResultSet.
     */
    private static final String USER_ID = "user_id";

    /**
     * Costante per il nome della colonna email nel database.
     */
    private static final String EMAIL = "email";

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param dataSource Il DataSource configurato per l'accesso al database.
     */
    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Cerca un utente nel database tramite l'indirizzo email (match esatto).
     * <p>
     * Utilizzata principalmente per il login, recupera tutti i dati sensibili dell'utente
     * (inclusa la password hashata) per la verifica delle credenziali.
     * </p>
     *
     * @param email L'email dell'utente da cercare.
     * @return Il {@code UserDTO} popolato se l'utente esiste, altrimenti {@code null}.
     * @throws SQLException In caso di errori durante l'esecuzione della query.
     */
    public UserDTO searchUserByMail(String email) throws SQLException{

        UserDTO foundedUser = null;

        String query = "SELECT U.* FROM User_ U WHERE email LIKE ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL));
                foundedUser.setPassword(rs.getString("hashed_password"));
                foundedUser.setRole(rs.getInt("user_type"));

            }

            rs.close();

        }

        return foundedUser;
    }

    /**
     * Registra un nuovo utente nel database.
     * <p>
     * Inserisce un nuovo record nella tabella 'User_' con i dati forniti.
     * </p>
     *
     * @param newUser Il DTO contenente email, password (già hashata) e ruolo.
     * @throws SQLException In caso di errori durante l'inserimento (es email duplicata).
     */
    public void registerNewUser(UserDTO newUser) throws SQLException{

        String query = "INSERT INTO User_ (email, hashed_password, user_type) VALUES "+
                "(?, ?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getPassword());
            statement.setInt(3, newUser.getRole());

            statement.executeUpdate();


        }

    }

    /**
     * Cerca sviluppatori o amministratori all'interno di un progetto specifico.
     * <p>
     * Esegue una JOIN con la tabella 'Works_on' per trovare utenti che lavorano al progetto indicato
     * e che hanno un ruolo privilegiato (user_type > 0).
     * </p>
     *
     * @param email     Stringa di ricerca per l'email (match parziale).
     * @param projectId L'ID del progetto in cui cercare.
     * @return Lista di utenti trovati.
     * @throws SQLException In caso di errori durante la ricerca.
     */
    public List<UserDTO> searchDevOrAdminByEmailAndProject(String email, Integer projectId) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U NATURAL JOIN Works_on W " +
                       "WHERE U.email ILIKE ? AND U.user_type > 0 AND W.project_id = ?;";

        searchResult = performSearch(email, projectId, query);

        return searchResult;


    }

    /**
     * Cerca membri all'interno di un team specifico.
     * <p>
     * Esegue una JOIN con la tabella 'Works_in' per trovare utenti che appartengono al team indicato.
     * </p>
     *
     * @param email  Stringa di ricerca per l'email (match parziale).
     * @param teamId L'ID del team in cui cercare.
     * @return Lista di utenti trovati.
     * @throws SQLException In caso di errori durante la ricerca.
     */
    public List<UserDTO> searchDevOrAdminByEmailAndTeam(String email, Integer teamId) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U NATURAL JOIN Works_in W " +
                "WHERE U.email ILIKE ? AND W.team_id = ?;";

        searchResult = performSearch(email, teamId, query);

        return searchResult;


    }

    /**
     * Metodo helper privato per eseguire le ricerche parametrizzate.
     * <p>
     * Evita la duplicazione di codice gestendo la preparazione dello statement,
     * l'impostazione dei parametri (email con wildcard e ID oggetto) e il mapping del ResultSet.
     * </p>
     *
     * @param email    La stringa parziale dell'email da cercare.
     * @param objectId L'ID del contesto (progetto o team).
     * @param query    La query SQL da eseguire.
     * @return Una lista di UserDTO popolata con i risultati.
     * @throws SQLException In caso di errori SQL.
     */
    private List<UserDTO> performSearch(String email, Integer objectId, String query) throws SQLException {

        List<UserDTO> searchResult;
        String emailToSearch = "%" + email + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, emailToSearch);
            statement.setInt(2, objectId);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                UserDTO foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL) );

                searchResult.add(foundedUser);


            }

            rs.close();

        }
        return searchResult;
    }

    /**
     * Cerca globalmente sviluppatori o amministratori nel sistema.
     * <p>
     * Filtra gli utenti in base al ruolo (user_type > 0) ignorando gli utenti base.
     * Utilizza ILIKE per una ricerca case-insensitive sull'email.
     * </p>
     *
     * @param email La stringa parziale dell'email da cercare.
     * @return Lista di utenti con ruolo Dev o Admin trovati.
     * @throws SQLException In caso di errori durante la ricerca.
     */
    public List<UserDTO> searchDevOrAdminByEmail(String email) throws SQLException{

        List<UserDTO> searchResult;

        String query = "SELECT * FROM User_ U " +
                       "WHERE U.email ILIKE ? AND U.user_type > 0;";

        String emailToSearch = "%" + email + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, emailToSearch);

            ResultSet rs = statement.executeQuery();

            searchResult = new ArrayList<>();

            while (rs.next()) {

                UserDTO foundedUser = new UserDTO();

                foundedUser.setId(rs.getInt(USER_ID));
                foundedUser.setEmail(rs.getString(EMAIL) );

                searchResult.add(foundedUser);

            }

            rs.close();

        }

        return searchResult;

    }

}
