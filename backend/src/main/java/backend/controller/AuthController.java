package backend.controller;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * Controller REST per la gestione dell'autenticazione degli utenti.
 * <p>
 * Questa classe espone gli endpoint per l'accesso (login) al sistema e per la registrazione.
 * Si occupa di verificare le credenziali dell'utente e, in caso di successo,
 * generare un token JWT (JSON Web Token) per le successive richieste autenticate.
 * </p>
 */
@RestController
public class AuthController {

    /**
     * Componente di accesso ai dati (DAO) per le operazioni sugli utenti.
     * Viene utilizzato per recuperare le informazioni utente dal database durante il login
     * e per salvare i nuovi utenti durante la registrazione.
     */
    private final UserDAO userDao;

    /**
     * Componente per la codifica e la verifica delle password.
     * Utilizzato per confrontare la password fornita nel login con l' hash salvato
     * e per effettuare l' hashing della password di un nuovo utente prima del salvataggio.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Chiave segreta utilizzata per la firma e la verifica dei token JWT.
     * Il valore viene iniettato automaticamente dal file di configurazione (application.properties)
     * tramite l'annotazione {@code @Value}.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param userDao         DAO per l'accesso ai dati utente.
     * @param passwordEncoder Encoder per la gestione sicura delle password.
     */
    public AuthController(UserDAO userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gestisce la richiesta di login dell'utente.
     * <p>
     * Verifica se l'email esiste nel database e se la password fornita corrisponde all' hash memorizzato.
     * Se le credenziali sono valide, genera un token JWT firmato valido per 24 ore.
     * </p>
     *
     * @param request Oggetto contenente email e password dell'utente.
     * @return ResponseEntity contenente il token JWT e i dati dell'utente se il login ha successo.
     * @throws SQLException In caso di errori di accesso al database.
     * @throws ResponseStatusException Con stato 401 (UNAUTHORIZED) se le credenziali sono errate.
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) throws SQLException {

        Optional<UserDTO> userOpt = Optional.ofNullable(userDao.searchUserByMail(request.getEmail()));

        if (userOpt.isPresent()) {
            UserDTO userDto = userOpt.get();

            if (passwordEncoder.matches(request.getPassword(), userDto.getPassword())) {

                String token = Jwts.builder()
                        .setSubject(userDto.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .compact();

                userDto.setPassword(null);

                return ResponseEntity.ok(new AuthResponse(token, userDto));
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

    }

    /**
     * DTO per la richiesta di login.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {

        private String email;
        private String password;

    }

    /**
     * DTO per la risposta di autenticazione contenente il token e i dettagli utente.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {

        private String token;
        private UserDTO user;

    }

    /**
     * Gestisce la richiesta di registrazione di un nuovo utente.
     * <p>
     * Verifica che l'email non sia già presente nel sistema, esegue l' hashing della password
     * e salva il nuovo utente tramite il DAO.
     * </p>
     *
     * @param request Oggetto contenente email, password e ruolo del nuovo utente.
     * @return ResponseEntity con un messaggio di successo o errore (400) se l'email è già in uso.
     * @throws SQLException In caso di errori durante il salvataggio nel database.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws SQLException {

        if (userDao.searchUserByMail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Error: email already used!");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserDTO newUser = new UserDTO();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setRole(request.getRole());
        userDao.registerNewUser(newUser);

        return ResponseEntity.ok("Registration success!");

    }

    /**
     * DTO per la richiesta di registrazione.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {

        private String email;
        private String password;
        private int role;

    }
}