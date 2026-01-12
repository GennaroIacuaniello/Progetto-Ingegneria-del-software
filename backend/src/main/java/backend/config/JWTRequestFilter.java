package backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro personalizzato per la gestione dell'autenticazione tramite JSON Web Token (JWT).
 * <p>
 * Questa classe estende {@link OncePerRequestFilter}, garantendo che il filtro venga eseguito
 * una sola volta per ogni richiesta HTTP in entrata. Il suo scopo principale è intercettare
 * le richieste, estrarre il token JWT dall'header "Authorization", validarlo e, se corretto,
 * impostare l'autenticazione dell'utente nel contesto di sicurezza di Spring ({@link SecurityContextHolder}).
 * </p>
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    /**
     * Data Access Object per recuperare le informazioni dell'utente dal database.
     * Viene utilizzato per verificare che l'utente estratto dal token esista effettivamente.
     */
    private static final Logger jwtLogger = LoggerFactory.getLogger(JWTRequestFilter.class);

    /**
     * La chiave segreta utilizzata per firmare e verificare i token JWT.
     * Deve corrispondere alla chiave utilizzata durante la fase di generazione del token.
     * <p>
     * Nota: In un ambiente di produzione, questa chiave dovrebbe essere molto più complessa
     * e caricata da variabili d'ambiente o file di configurazione sicuri.
     * </p>
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Esegue la logica di filtraggio per ogni richiesta HTTP.
     * <p>
     * Il metodo segue questi passaggi:
     * <ol>
     * <li>Controlla l'header "Authorization" per verificare la presenza del prefisso "Bearer ".</li>
     * <li>Estrae il token JWT e lo analizza per ottenere lo username (subject).</li>
     * <li>Se lo username è valido e non c'è già un'autenticazione in corso, carica i dettagli dell'utente dal DB.</li>
     * <li>Verifica la validità del token (corrispondenza username e data di scadenza).</li>
     * <li>Se il token è valido, crea un oggetto di autenticazione e lo imposta nel {@link SecurityContextHolder},
     * permettendo a Spring Security di riconoscere l'utente come loggato.</li>
     * </ol>
     * </p>
     *
     * @param request  La richiesta HTTP in arrivo.
     * @param response La risposta HTTP in uscita.
     * @param chain    La catena di filtri attraverso cui deve passare la richiesta.
     * @throws ServletException In caso di errori durante l'elaborazione della servlet.
     * @throws IOException      In caso di errori di input/output.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                jwtLogger.warn("Token not valid or expired: {}", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request, response);
    }
}
